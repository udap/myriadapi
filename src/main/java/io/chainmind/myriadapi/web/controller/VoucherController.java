package io.chainmind.myriadapi.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
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
import io.chainmind.myriad.domain.dto.voucher.QualifyVoucherRequest;
import io.chainmind.myriad.domain.dto.voucher.QualifyVoucherResponse;
import io.chainmind.myriad.domain.dto.voucher.TransferVoucherRequest;
import io.chainmind.myriad.domain.dto.voucher.TransferVoucherResponse;
import io.chainmind.myriad.domain.dto.voucher.UsageStatus;
import io.chainmind.myriad.domain.dto.voucher.VoucherListItem;
import io.chainmind.myriad.domain.dto.voucher.VoucherResponse;
import io.chainmind.myriad.domain.dto.voucher.config.DiscountResponse;
import io.chainmind.myriad.domain.dto.voucher.config.SimpleVoucherConfig;
import io.chainmind.myriadapi.client.VoucherClient;
import io.chainmind.myriadapi.domain.CodeName;
import io.chainmind.myriadapi.domain.RequestUser;
import io.chainmind.myriadapi.domain.dto.ApiQualifyCouponsRequest;
import io.chainmind.myriadapi.domain.dto.Code;
import io.chainmind.myriadapi.domain.dto.OrgDTO;
import io.chainmind.myriadapi.domain.dto.VoucherDetailsResponse;
import io.chainmind.myriadapi.domain.entity.Account;
import io.chainmind.myriadapi.domain.entity.AuthorizedMerchant;
import io.chainmind.myriadapi.domain.entity.Organization;
import io.chainmind.myriadapi.domain.exception.ApiException;
import io.chainmind.myriadapi.service.AccountService;
import io.chainmind.myriadapi.service.AuthorizedMerchantService;
import io.chainmind.myriadapi.service.OrganizationService;
import io.chainmind.myriadapi.service.ValidationUtils;
import io.chainmind.myriadapi.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/vouchers")
public class VoucherController {
	@Autowired
	private VoucherClient voucherClient;

	@Autowired
	private RequestUser requestUser;
	
	@Autowired
	private AccountService accountService;
	@Autowired
	private AuthorizedMerchantService merchantService;
	@Autowired
	private OrganizationService organizationService;
	
	/**
	 * TODO: remove merchantCode and codeType to ensure returning only owner's vouchers
	 * @param page
	 * @param size
	 * @param sort
	 * @param ownerId
	 * @param status
	 * @param merchantCode
	 * @param idType
	 * @param codeType
	 * @param type
	 * @return
	 */
	@GetMapping
	public PaginatedResponse<VoucherListItem> getVouchers(
            @RequestParam(name="page", required=false, defaultValue="0") int page,
            @RequestParam(name="size", required=false, defaultValue="20") int size,
            @RequestParam(name="sort", required=false, defaultValue="createdAt:desc") String sort,
            @RequestParam(name="ownerId", required = true) String ownerId,
            @RequestParam(name="campaignId", required = false) String campaignId,
            @RequestParam(name="status", required = false) UsageStatus status,
            @RequestParam(name="merchantCode", required = false) String merchantCode,
            @RequestParam(name="idType", required = false, defaultValue="ID") CodeName idType,
            @RequestParam(name="codeType", required = false, defaultValue="ID") CodeName codeType,
            @RequestParam(name="type", required = false) VoucherType type) {
		
		PageRequest pageRequest = PageRequest.of(page, size, CommonUtils.parseSort(sort));
		log.debug("GET /api/vouchers: sorts: {}, ownerId:{}", pageRequest.getSort(), ownerId);
		// query account id based on given ownerId and idType
		Code ownerCode = Code.builder()
				.value(ownerId)
				.name(idType)
				.build();
		String accountIds = getAccountIdsByCode(ownerCode);		
		if (!StringUtils.hasText(accountIds))
			// new account, return an empty list response
			return emptyResponse();

		log.debug("getVouchers.account: {}", accountIds);

		// TODO: we must ensure the user of the account has granted permissions to current app or the account is
		// owned by current app or organization
		
		// query merchant id
		String merchantId = null;
		if (StringUtils.hasText(merchantCode)) {
			try {
				Code mCode = CommonUtils.uniqueCode(requestUser.getAppOrg().getId().toString(), 
						Code.builder().name(codeType).value(merchantCode).build());
				Organization merchant = organizationService.findByCode(mCode.getValue(), mCode.getName());
				merchantId = merchant.getId().toString();
				// this is a quick hack to allow returning top ancestor's vouchers
				Organization topAncestor = organizationService.findTopAncestor(merchant);
				if (Objects.nonNull(topAncestor) && Objects.nonNull(merchant) 
						&& !Objects.equals(topAncestor.getId(), merchant.getId())) {
					merchantId = merchant.getId().toString().concat(",").concat(topAncestor.getId().toString());
				}
				
			} catch(Exception e) {
				log.warn("query merchant by code {} and type {} raised exception {}", merchantCode, codeType, e.getMessage());
				return emptyResponse();
			}

			log.debug("merchant ids: {}", merchantId);
		}
		
		// excludes NEW vouchers
		return voucherClient.queryVouchers(pageRequest, accountIds, campaignId, null, 
				merchantId, type, status, true, null);
	}
	
	private String getAccountIdsByCode(Code code) {
		String accountIds = null;
		if (CodeName.MIXED.equals(code.getName())) {
			List<String> accountIdList = new ArrayList<>();
			// ownerId is in a mixed format like CELLPHONE:18621991234,SOURCE_ID:123,CELLPHONE:17600786111
			List<Code> codes = CommonUtils.parseMixedCode(code.getValue());
			codes.forEach(mc->{
				Account account = getOrCreateAccount(mc.getValue(), mc.getName());	
				if (account != null)
					accountIdList.add(account.getId().toString());
			});
			// convert to comma-separated list
			if (!accountIdList.isEmpty())
				accountIds = StringUtils.collectionToCommaDelimitedString(accountIdList);
		} else {
			Account account = getOrCreateAccount(code.getValue(),code.getName());
			if (account != null)
				accountIds = account.getId().toString();
		}	
		return accountIds;
	}
		
	private Account getOrCreateAccount(String ownerId, CodeName idType) {
		Code aCode = Code.builder()
				.value(ownerId).name(idType)
				.build();
		aCode = CommonUtils.uniqueCode(requestUser.getAppOrg().getId().toString(), aCode);
		Account account = accountService.findByCode(aCode.getValue(), aCode.getName());	
		// try to register an account
		if (account == null) {
			// try to register an account
			if(!CodeName.ID.equals(idType)) {
				account = accountService.register(aCode.getValue(), aCode.getName());	
			}
			else {
				log.warn("query account by id {} returned null", ownerId);
			}
		}
		return account;
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
    	VoucherResponse voucher = voucherClient.findVoucherById(voucherId);
    	log.debug("findVoucherById: " + voucher.getId());
    	
    	// TODO: disallow query if voucher's issuer is not the appOrg or is not a subsidiary of current appOrg
    	
    	// replace issuer (id) with issuer name
    	voucher.setIssuer(organizationService.findById(Long.valueOf(voucher.getIssuer())).getName());
    	
    	// query merchant data based on merchant id
    	Set<OrgDTO> merchants = new TreeSet<OrgDTO>();
    	for (String id: voucher.getMerchants()) {
    		Organization merchant = organizationService.findById(Long.valueOf(id));
    		// query all subsidiaries
    		List<Organization> outlets = organizationService.getDescendants(merchant);
    		if (!CollectionUtils.isEmpty(outlets)) {
    			outlets.forEach(o->{
    				merchants.add(OrgDTO.builder()
    		    			.address(o.getFullAddress())
    		    			.name(o.getFullName())
    		    			.shortName(o.getName())
    		    			.phone(o.getPhone())
    		    			.id(o.getId().toString())
    		    			.build());
    			});
    		} else {
	    		merchants.add(OrgDTO.builder()
	    			.address(merchant.getFullAddress())
	    			.name(merchant.getFullName())
	    			.shortName(merchant.getName())
	    			.phone(merchant.getPhone())
	    			.id(merchant.getId().toString())
	    			.build());   
    		}
    	}
    	
    	VoucherDetailsResponse response = new VoucherDetailsResponse();
    	response.setAuthorizationCode(voucher.getAuthorizationCode());
    	response.setCampaign(voucher.getCampaign());
		response.setCode(voucher.getCode());
    	response.setCategory(voucher.getCategory());
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
		requestUser.setId(req.getReqUser());
    	return voucherClient.transferVoucher(voucherId, req);
    }
    
    @PostMapping("/batchTransfer")
    public BatchTransferResponse batchTransfer(@RequestBody @Valid BatchTransferRequest req) {
    	// validate the appOrg is an ancestor of the reqOrg
    	Organization reqOrg = organizationService.findById(Long.valueOf(req.getReqOrgId()));
    	if (reqOrg==null)
    		throw new ApiException(HttpStatus.NOT_FOUND, "batchTransfer.invalidParams");
    	Organization topAncestor = organizationService.findTopAncestor(reqOrg);
    	if (!Objects.equals(topAncestor, requestUser.getAppOrg()))
    		throw new ApiException(HttpStatus.FORBIDDEN, "batchTransfer.invalidParams");
		requestUser.setId(req.getReqUser());
    	return voucherClient.batchTransfer(req);
    }
        
    @PostMapping("/qualify")
    public List<VoucherListItem> queryQualifiedCoupons(@Valid @RequestBody ApiQualifyCouponsRequest req) {
    	String accountIds = getAccountIdsByCode(req.getAccountCode());
//    	Account account = accountService.findByCode(req.getCustomerCode().getId(), req.getCustomerCode().getType());
//    	if (account == null) {
//    		// create an account
//    		account = accountService.register(req.getCustomerCode().getId(), req.getCustomerCode().getType());
//    		return Collections.emptyList();
//    	}
		if (!StringUtils.hasText(accountIds))
			// new account, return an empty list response
			return Collections.emptyList();

    	// TODO: query merchant and its ancestors
		Code mCode = CommonUtils.uniqueCode(requestUser.getAppOrg().getId().toString(), req.getMerchantCode());
    	Organization merchant = organizationService.findByCode(mCode.getValue(), mCode.getName());
		Organization topAncestor = organizationService.findTopAncestor(merchant);
		boolean isTopAncestor = Objects.equals(topAncestor.getId(), merchant.getId());
    	    	
    	List<VoucherListItem> qualifiedVouchers = new ArrayList<>();
    	
    	int totalPages = 0;
    	int page = 0;
    	do {
    		// query active vouchers excluding NEW vouchers (do not specify merchant id because rules may be 
        	// based on merchant tags rather than specific merchant)
	    	PaginatedResponse<VoucherListItem> vouchers = voucherClient.queryVouchers(PageRequest.of(page++, 100), 
	    			accountIds, null, null, null, 
	    			VoucherType.COUPON, UsageStatus.ACTIVE, true, null);
	    	totalPages = vouchers.getTotal();
	    	for (VoucherListItem v : vouchers.getEntries()) {
				Organization marketer = organizationService.findById(Long.valueOf(v.getIssuer()));
	    		AuthorizedMerchant am = merchantService.find(marketer, merchant);
	    		AuthorizedMerchant amAncestor = (isTopAncestor)?am:merchantService.find(marketer, topAncestor);
	    		boolean merchantIsAuthorized = merchantService.existsInDescendant(marketer, merchant);
	    		boolean ancestorIsAuthorzied = merchantService.existsInDescendant(marketer, topAncestor);
	    		Optional<Merchant> m = ValidationUtils.prepareMerchantFacts(merchant, topAncestor, am, amAncestor, 
	    				merchantIsAuthorized, ancestorIsAuthorzied);
	    		if (!m.isPresent()) 
	    			continue;
	    		QualifyVoucherResponse result = voucherClient.qualifyVoucher(v.getId(), 
    				QualifyVoucherRequest.builder()
	    				.merchant(m.get())
	    				.order(req.getOrder())
	    				.id(v.getId())
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

//    /**
//     * Customer collects a voucher
//     * @param campaignId the campaign where the voucher is collected from
//     * @param customerId identifier of the customer who collects a voucher
//     * @param idType customer id type, e.g. ID, CELLPHONE, EMAIL or SOURCE_ID
//     * @return
//     */
//    @GetMapping("/collect")
//    public DistributeVoucherResponse create(
//            @RequestParam(name="campaignId", required = true) String campaignId,
//            @RequestParam(name="customerId", required = true) String customerId,
//            @RequestParam(name="idType", required = false, defaultValue="ID") CodeName idType 
//    	) {
//    	// find customer account
//    	Code aCode = CommonUtils.uniqueCode(requestUser.getAppOrg().getId().toString(), 
//    			Code.builder().value(customerId).name(idType).build());
//		Account account = accountService.findByCode(aCode.getValue(), aCode.getName());			
//		// try to register an account
//		if (account == null) {
//			// try to register an account
//			if(!CodeName.ID.equals(idType))
//				account = accountService.register(customerId, idType);	
//		}
//		if (account == null)
//			throw new ApiException(HttpStatus.NOT_FOUND,"account.notFound");
//		log.debug("Customer {} with account id {}", customerId, account.getId());
//		// create an audience object
//    	Audience audience = Audience.builder()
//    			.id(account.getId().toString())
//    			.build();
//    	CollectVoucherRequest req = CollectVoucherRequest.builder()
//    			.campaignId(campaignId)
//    			.channel(requestUser.getAppId()) // app
//    			.audience(audience)
//    			.build();
//    	log.debug("send request to voucher services...");
//    	return voucherClient.create(req);
//    }

}
