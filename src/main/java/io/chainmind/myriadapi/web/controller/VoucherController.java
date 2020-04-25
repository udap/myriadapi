package io.chainmind.myriadapi.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.chainmind.myriad.domain.common.VoucherType;
import io.chainmind.myriad.domain.dto.PaginatedResponse;
import io.chainmind.myriad.domain.dto.voucher.BatchTransferRequest;
import io.chainmind.myriad.domain.dto.voucher.BatchTransferResponse;
import io.chainmind.myriad.domain.dto.voucher.TransferVoucherRequest;
import io.chainmind.myriad.domain.dto.voucher.TransferVoucherResponse;
import io.chainmind.myriad.domain.dto.voucher.UsageStatus;
import io.chainmind.myriad.domain.dto.voucher.VoucherListItem;
import io.chainmind.myriad.domain.dto.voucher.VoucherResponse;
import io.chainmind.myriadapi.client.VoucherClient;
import io.chainmind.myriadapi.domain.CodeType;
import io.chainmind.myriadapi.domain.RequestOrg;
import io.chainmind.myriadapi.domain.dto.OrgDTO;
import io.chainmind.myriadapi.domain.dto.VoucherDetailsResponse;
import io.chainmind.myriadapi.domain.entity.Account;
import io.chainmind.myriadapi.domain.entity.Organization;
import io.chainmind.myriadapi.domain.exception.ApiException;
import io.chainmind.myriadapi.service.AccountService;
import io.chainmind.myriadapi.service.AuthorizedMerchantService;
import io.chainmind.myriadapi.service.OrganizationService;

@RestController
@RequestMapping("/api/vouchers")
public class VoucherController {
	private static final Logger LOG = LoggerFactory.getLogger(VoucherController.class);

	@Autowired
	private VoucherClient voucherClient;

	@Autowired
	private RequestOrg requestOrg;
	
	@Autowired
	private AccountService accountService;
	@Autowired
	private AuthorizedMerchantService merchantService;
	@Autowired
	private OrganizationService organizationService;
	
	@GetMapping
	public PaginatedResponse<VoucherListItem> getVouchers(
            @PageableDefault(size = 20, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(name="ownerId", required = true) String ownerId,
            @RequestParam(name="status", required = false) UsageStatus status,
            @RequestParam(name="merchantCode", required = false) String merchantCode,
            @RequestParam(name="idType", required = false, defaultValue="ID") CodeType idType,
            @RequestParam(name="codeType", required = false, defaultValue="ID") CodeType codeType,
            @RequestParam(name="type", required = false, defaultValue="COUPON") VoucherType type) {
		LOG.debug("ownerId: " + ownerId + ", idType: " + idType);
		// query account id based on given ownerId and idType
		Account account = accountService.findByCode(ownerId, idType);	
		
		// try to register an account
		if (account == null && !CodeType.ID.equals(idType)) {
			// try to register an account
			account = accountService.register(ownerId, idType);	
			// return an empty page
			PaginatedResponse<VoucherListItem> response = new PaginatedResponse<VoucherListItem>();
			response.setPage(0);
			response.setSize(0);
			response.setTotal(0);
			response.setEntries(Collections.emptyList());
			return response;
		}

		LOG.debug("account: "+ account.getId());
				
		// query merchant id
		String merchantId = StringUtils.hasText(merchantCode)
				?merchantService.getId(merchantCode, codeType):null;
		
		LOG.debug("merchant id: "+ merchantId);
		
		return voucherClient.queryVouchers(pageable, account.getId().toString(), null, null, 
				merchantId, type, status, null);
	}
	
    @GetMapping("/{id}")
	public VoucherDetailsResponse getVoucherById(@PathVariable(name = "id") String voucherId) {
    	LOG.debug("getVoucherById: " + voucherId);
    	VoucherResponse voucher = voucherClient.findVoucherById(voucherId);
    	LOG.debug("response: " + voucher.getId());
    	// replace issuer (id) with issuer name
    	voucher.setIssuer(organizationService.findById(Long.valueOf(voucher.getIssuer())).getName());
    	
    	// query merchant data based on merchant id
    	List<OrgDTO> merchants = new ArrayList<OrgDTO>();
    	for (String id: voucher.getMerchants()) {
    		Organization merchant = organizationService.findById(Long.valueOf(id));
    		merchants.add(OrgDTO.builder()
    			.address(merchant.getFullAddress())
    			.name(merchant.getFullName())
    			.shortName(merchant.getName())
    			.phone(merchant.getPhone())
    			.id(merchant.getId().toString())
    			.build());    		
    	}
    	
    	VoucherDetailsResponse response = new VoucherDetailsResponse();
    	response.setAuthorizationCode(voucher.getAuthorizationCode());
    	response.setCampaign(voucher.getCampaign());
    	response.setCategory(voucher.getCode());
    	response.setConfig(voucher.getConfig());
    	response.setEffective(voucher.getEffective());
    	response.setExpiry(voucher.getExpiry());
    	response.setId(voucher.getId());
    	response.setIssuer(voucher.getIssuer());
    	response.setMetadata(voucher.getMetadata());
    	response.setOwner(voucher.getOwner());
    	response.setRedeemedQuantity(voucher.getRedeemedQuantity());
    	response.setRules(voucher.getRules());
    	response.setStatus(voucher.getStatus());
    	response.setUpdatedAt(voucher.getUpdatedAt());
    	response.setMerchants(merchants);
    	return response;
    }

    @PutMapping("/{id}/transfer")
    public TransferVoucherResponse transfer(@PathVariable(name="id")String voucherId, 
    		@Valid @RequestBody TransferVoucherRequest req) {
    	return voucherClient.transferVoucher(voucherId, req);
    }
    
    @PostMapping("/batchTransfer")
    public BatchTransferResponse batchTransfer(@RequestBody @Valid BatchTransferRequest req) {
    	// TODO: validate the reqOrg is the requestOrg or is a subsidiary of the requestOrg
    	if (!requestOrg.getAppOrg().getId().toString().equals(req.getReqOrgId()))
    		throw new ApiException(HttpStatus.BAD_REQUEST, "transfer.invalidOperation");
    	return voucherClient.batchTransfer(req);
    }


}
