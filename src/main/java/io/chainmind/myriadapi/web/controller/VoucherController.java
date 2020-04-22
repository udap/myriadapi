package io.chainmind.myriadapi.web.controller;

import java.util.Collections;

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

import io.chainmind.myriad.domain.common.VoucherStatus;
import io.chainmind.myriad.domain.common.VoucherType;
import io.chainmind.myriad.domain.dto.PaginatedResponse;
import io.chainmind.myriad.domain.dto.voucher.VoucherListItem;
import io.chainmind.myriad.domain.dto.voucher.VoucherResponse;
import io.chainmind.myriadapi.client.VoucherClient;
import io.chainmind.myriadapi.domain.CodeType;
import io.chainmind.myriadapi.domain.entity.Account;
import io.chainmind.myriadapi.service.AccountService;
import io.chainmind.myriadapi.service.AuthorizedMerchantService;
import io.chainmind.myriadapi.service.OrganizationService;

@RestController
@RequestMapping("/vouchers")
public class VoucherController {
	
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
            @RequestParam(name="status", required = true) VoucherStatus status,
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
		
		return voucherClient.list(pageable, account.getId().toString(), null, null, merchantId, type, null);
	}
	
    @GetMapping("/{id}")
	public VoucherResponse getVoucherById(@PathVariable(name = "id") String voucherId) {
    	VoucherResponse response = voucherClient.findById(voucherId);
    	// replace issuer (id) with issuer name
    	response.setIssuer(organizationService.findById(Long.valueOf(response.getIssuer())).getName());
    	return response;
    }

}
