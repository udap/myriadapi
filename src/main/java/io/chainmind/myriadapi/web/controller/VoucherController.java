package io.chainmind.myriadapi.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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

import io.chainmind.myriad.domain.common.DiscountType;
import io.chainmind.myriad.domain.common.Merchant;
import io.chainmind.myriad.domain.common.Order;
import io.chainmind.myriad.domain.common.VoucherType;
import io.chainmind.myriad.domain.dto.PaginatedResponse;
import io.chainmind.myriad.domain.dto.voucher.BatchTransferRequest;
import io.chainmind.myriad.domain.dto.voucher.BatchTransferResponse;
import io.chainmind.myriad.domain.dto.voucher.QualifyRequest;
import io.chainmind.myriad.domain.dto.voucher.QualifyResult;
import io.chainmind.myriad.domain.dto.voucher.TransferVoucherRequest;
import io.chainmind.myriad.domain.dto.voucher.TransferVoucherResponse;
import io.chainmind.myriad.domain.dto.voucher.UsageStatus;
import io.chainmind.myriad.domain.dto.voucher.VoucherListItem;
import io.chainmind.myriad.domain.dto.voucher.VoucherResponse;
import io.chainmind.myriad.domain.dto.voucher.config.DiscountResponse;
import io.chainmind.myriad.domain.dto.voucher.config.SimpleVoucherConfig;
import io.chainmind.myriadapi.client.VoucherClient;
import io.chainmind.myriadapi.domain.CodeType;
import io.chainmind.myriadapi.domain.RequestOrg;
import io.chainmind.myriadapi.domain.dto.OrgDTO;
import io.chainmind.myriadapi.domain.dto.QualifyCouponRequest;
import io.chainmind.myriadapi.domain.dto.VoucherDetailsResponse;
import io.chainmind.myriadapi.domain.entity.Account;
import io.chainmind.myriadapi.domain.entity.AuthorizedMerchant;
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
	private RequestOrg appOrg;
	
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
		if (account == null) {
			// try to register an account
			if(!CodeType.ID.equals(idType))
				account = accountService.register(ownerId, idType);	
			// new account, return an empty list response
			return emptyResponse();
		}
		LOG.debug("getVouchers.account: {}", account.getId());
		
		// query merchant id
		String merchantId = null;
		if (StringUtils.hasText(merchantCode)) {
			try {
				Organization merchant = organizationService.findByCode(merchantCode, codeType);
				merchantId = merchant.getId().toString();
			} catch(Exception e) {
				LOG.info("getVouchers.merchant({}): {}", merchantCode, e.getMessage());
				return emptyResponse();
			}
		}
		LOG.debug("getVouchers.merchantId: {}", merchantId);
		
		// excludes NEW vouchers
		return voucherClient.queryVouchers(pageable, account.getId().toString(), null, null, 
				merchantId, type, status, true, null);
	}
	
	private PaginatedResponse<VoucherListItem> emptyResponse() {
		PaginatedResponse<VoucherListItem> response = new PaginatedResponse<VoucherListItem>();
		response.setPage(0);
		response.setSize(0);
		response.setTotal(0);
		response.setEntries(Collections.emptyList());
		return response;		
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
    	// validate the appOrg is an ancestor of the reqOrg
    	Organization reqOrg = organizationService.findById(Long.valueOf(req.getReqOrgId()));
    	if (reqOrg==null)
    		throw new ApiException(HttpStatus.NOT_FOUND, "batchTransfer.invalidParams");
    	Organization topAncestor = organizationService.findTopAncestor(reqOrg);
    	if (!Objects.equals(topAncestor, appOrg.getAppOrg()))
    		throw new ApiException(HttpStatus.FORBIDDEN, "batchTransfer.invalidParams");

    	return voucherClient.batchTransfer(req);
    }
    
    @PostMapping("/qualify")
    public List<VoucherListItem> qualifyCoupons(@Valid @RequestBody QualifyCouponRequest req) {
    	Organization merchant = organizationService.findByCode(req.getMerchantCode().getId(), req.getMerchantCode().getType());
		Organization topAncestor = organizationService.findTopAncestor(merchant);
		boolean isTopAncestor = Objects.equals(topAncestor.getId(), merchant.getId());
    	
    	Account account = accountService.findByCode(req.getCustomerCode().getId(), req.getCustomerCode().getType());
    	if (account == null) {
    		// create an account
    		account = accountService.register(req.getCustomerCode().getId(), req.getCustomerCode().getType());
    	}
    	
    	List<VoucherListItem> qualifiedVouchers = new ArrayList<>();

    	int totalPages = 0;
    	int page = 0;
    	do {
    		// query active vouchers excluding NEW vouchers (do not specify merchant id because rules may be 
        	// based on merchant tags rather than specific merchant)
	    	PaginatedResponse<VoucherListItem> vouchers = voucherClient.queryVouchers(PageRequest.of(page++, req.getLimit()), 
	    			account.getId().toString(), null, null, null, 
	    			VoucherType.COUPON, UsageStatus.ACTIVE, true, null);
	    	totalPages = vouchers.getTotal();
	    	for (VoucherListItem v : vouchers.getEntries()) {
				Organization marketer = organizationService.findById(Long.valueOf(v.getIssuer()));
	    		AuthorizedMerchant am = merchantService.find(marketer, merchant);
	    		AuthorizedMerchant amAncestor = (isTopAncestor)?am:merchantService.find(marketer, topAncestor);
	    		// neither merchant nor its top ancestor is not authorized 
	    		if (am == null && amAncestor == null)
	    			continue;
	    		Merchant mAncestor = isTopAncestor? null: Merchant.builder()
	    				.id(topAncestor.getId().toString())
	    				.province(topAncestor.getProvince())
	    				.city(topAncestor.getCity())
	    				.district(topAncestor.getDistrict())
	    				.tags(amAncestor.getTags())
	    				.build();
	    		Merchant m = Merchant.builder()
	    				.id(merchant.getId().toString())
	    				.province(merchant.getProvince())
	    				.city(merchant.getCity())
	    				.district(merchant.getDistrict())
	    				.tags((am!=null)?am.getTags():null)
	    				.topAncestor(mAncestor)
	    				.build();
	    		QualifyResult result = voucherClient.qualify(v.getId(), QualifyRequest.builder()
	    				.customerId(account.getId().toString())
	    				.merchant(m)
	    				.order(req.getOrder())
	    				.voucherId(v.getId())
	    				.build());
	    		if (result.isQualified())
	    			qualifiedVouchers.add(v);
	    	
	    		if (qualifiedVouchers.size()>=req.getLimit())
	    			break;
	    	}
	    	
    	} while (page < totalPages && qualifiedVouchers.size() < req.getLimit());
    	
    	// sort by discount value
    	Collections.sort(qualifiedVouchers, new Comparator<VoucherListItem>() {
			@Override
			public int compare(VoucherListItem v1, VoucherListItem v2) {
				SimpleVoucherConfig c1 = v1.getConfig();
				SimpleVoucherConfig c2 = v2.getConfig();
				
				// calculate discount value
				Integer valueOff1 = discountAmount(c1.getDiscount(), req.getOrder());
				Integer valueOff2 = discountAmount(c2.getDiscount(), req.getOrder());
				
				//no order
				if (req.getOrder() == null || req.getOrder().getAmount() == null) {
					if (c1.getDiscount().getType().equals(c2.getDiscount().getType()))
						return valueOff2.compareTo(valueOff1);
					else 
						return c2.getDiscount().getType().compareTo(c1.getDiscount().getType());   						
				} 
				
				if (valueOff1 == valueOff2)
					return v1.getExpiry().compareTo(v2.getExpiry());
				else
					return valueOff2.compareTo(valueOff1);
			}
    	});
    	
    	
    	return qualifiedVouchers;
    }
    
    private Integer discountAmount(DiscountResponse discount, Order order) {
    	if (order == null || order.getAmount() == null)
    		return discount.getValueOff();
    	if (discount.getType().equals(DiscountType.AMOUNT))
    		return discount.getValueOff();
    	else if (discount.getType().equals(DiscountType.PERCENT)) {
    		int valueOff = BigDecimal.valueOf(discount.getValueOff())
    				.divide(BigDecimal.valueOf(100))
    				.multiply(BigDecimal.valueOf(order.getAmount())).intValue();
    		if (discount.getAmountLimit()!=null)
    			valueOff = valueOff > discount.getAmountLimit()? discount.getAmountLimit() : valueOff;
    		return valueOff;
    	}
    	return 0;
    }



}
