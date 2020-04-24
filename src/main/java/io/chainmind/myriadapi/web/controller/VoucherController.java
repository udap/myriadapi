package io.chainmind.myriadapi.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.chainmind.myriad.domain.common.VoucherType;
import io.chainmind.myriad.domain.dto.PaginatedResponse;
import io.chainmind.myriad.domain.dto.voucher.UsageStatus;
import io.chainmind.myriad.domain.dto.voucher.VoucherListItem;
import io.chainmind.myriad.domain.dto.voucher.VoucherResponse;
import io.chainmind.myriadapi.client.VoucherClient;
import io.chainmind.myriadapi.domain.CodeType;
import io.chainmind.myriadapi.domain.dto.OrgDTO;
import io.chainmind.myriadapi.domain.dto.VoucherDetailsResponse;
import io.chainmind.myriadapi.domain.entity.Account;
import io.chainmind.myriadapi.domain.entity.Organization;
import io.chainmind.myriadapi.service.AccountService;
import io.chainmind.myriadapi.service.AuthorizedMerchantService;
import io.chainmind.myriadapi.service.OrganizationService;

@RestController
@RequestMapping("/vouchers")
public class VoucherController {
	private static final Logger LOG = LoggerFactory.getLogger(VoucherController.class);

	@Autowired
	private VoucherClient voucherClient;
	
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
		
		// query account id based on given ownerId and idType
		Account account = accountService.findByCode(ownerId, idType);	
		
		// try to register an account
		if (account == null && !CodeType.ID.equals(idType)) {
			// try to register an account
			account = accountService.register(ownerId, idType);	
		}
		
		if (account == null) {
			PaginatedResponse<VoucherListItem> response = new PaginatedResponse<VoucherListItem>();
			response.setPage(0);
			response.setSize(0);
			response.setTotal(0);
			response.setEntries(Collections.emptyList());
			return response;
		}
		
		// query merchant id
		String merchantId = StringUtils.hasText(merchantCode)
				?merchantService.getId(merchantCode, codeType):null;
		
		return voucherClient.list(pageable, account.getId().toString(), null, null, 
				merchantId, type, status, null);
	}
	
    @GetMapping("/{id}")
	public VoucherDetailsResponse getVoucherById(@PathVariable(name = "id") String voucherId) {
    	LOG.debug("getVoucherById: " + voucherId);
    	VoucherResponse voucher = voucherClient.findById(voucherId);
    	LOG.debug("response: " + voucher.getId());
    	// replace issuer (id) with issuer name
    	voucher.setIssuer(organizationService.findById(Long.valueOf(voucher.getIssuer())).getName());
    	
    	// query merchant data based on merchant id
    	List<OrgDTO> merchants = new ArrayList<OrgDTO>();
    	for (String id: voucher.getMerchants()) {
    		Organization merchant = organizationService.findById(Long.valueOf(id));
    		merchants.add(OrgDTO.builder()
    			.address(merchant.getFullAddress())
    			.name(merchant.getName())
    			.phone(merchant.getPhone())
    			.uid(merchant.getUid())
    			.build());    		
    	}
    	return VoucherDetailsResponse.builder()
    		.authorizationCode(voucher.getAuthorizationCode())
    		.campaign(voucher.getCampaign())
    		.category(voucher.getCode())
    		.config(voucher.getConfig())
    		.effective(voucher.getEffective())
    		.expiry(voucher.getExpiry())
    		.id(voucher.getId())
    		.issuer(voucher.getIssuer())
    		.metadata(voucher.getMetadata())
    		.owner(voucher.getOwner())
    		.redeemedQuantity(voucher.getRedeemedQuantity())
    		.rules(voucher.getRules())
    		.status(voucher.getStatus())
    		.updatedAt(voucher.getUpdatedAt())
    		.merchants(merchants)
    		.build();
    }

}
