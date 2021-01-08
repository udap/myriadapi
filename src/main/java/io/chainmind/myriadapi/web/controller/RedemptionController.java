package io.chainmind.myriadapi.web.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.chainmind.myriad.domain.common.Merchant;
import io.chainmind.myriad.domain.dto.redemption.ConfirmRedemptionRequest;
import io.chainmind.myriad.domain.dto.redemption.ConfirmRedemptionResponse;
import io.chainmind.myriad.domain.dto.redemption.CreateRedemptionRequest;
import io.chainmind.myriad.domain.dto.redemption.CreateRedemptionResponse;
import io.chainmind.myriad.domain.dto.voucher.VoucherResponse;
import io.chainmind.myriadapi.client.VoucherClient;
import io.chainmind.myriadapi.domain.RequestUser;
import io.chainmind.myriadapi.domain.dto.Code;
import io.chainmind.myriadapi.domain.dto.CompleteRedemptionRequest;
import io.chainmind.myriadapi.domain.dto.RedeemVoucherRequest;
import io.chainmind.myriadapi.domain.entity.Account;
import io.chainmind.myriadapi.domain.entity.AuthorizedMerchant;
import io.chainmind.myriadapi.domain.entity.Organization;
import io.chainmind.myriadapi.domain.exception.ApiException;
import io.chainmind.myriadapi.service.AccountService;
import io.chainmind.myriadapi.service.AuthorizedMerchantService;
import io.chainmind.myriadapi.service.OrganizationService;
import io.chainmind.myriadapi.service.ValidationUtils;
import io.chainmind.myriadapi.utils.CommonUtils;

@RestController
@RequestMapping("/api/redemptions")
public class RedemptionController {
	
	@Autowired
	private VoucherClient redemptionClient;
	@Autowired
	private AccountService accountService;
	@Autowired
	private AuthorizedMerchantService merchantService;
	@Autowired
	private OrganizationService organizationService;
//	@Autowired
//	private RequestUser requestOrg;
	@Autowired
	private RequestUser requestUser;

	@PostMapping
	public CreateRedemptionResponse create(@Valid @RequestBody RedeemVoucherRequest req) {
		CreateRedemptionRequest redeemReq = new CreateRedemptionRequest();
		redeemReq.setOrderId(req.getOrder().getId());
		redeemReq.setVoucherId(req.getVoucherId());
		// query account
		Code aCode = CommonUtils.uniqueCode(requestUser.getAppOrg().getId().toString(), req.getAccountCode());
		Account account = accountService.findByCode(aCode.getValue(), aCode.getName());
		if (account == null)
			throw new ApiException(HttpStatus.NOT_FOUND, "user.notFound");
		redeemReq.setCustomerId(account.getId().toString());
		redeemReq.setReqUser(redeemReq.getCustomerId());
		// query merchant
		Code mCode = CommonUtils.uniqueCode(requestUser.getAppOrg().getId().toString(), req.getMerchantCode());
		Organization merchant = organizationService.findByCode(mCode.getValue(),mCode.getName()); 
		
		VoucherResponse voucher = redemptionClient.findVoucherById(req.getVoucherId());
		// do not allow a customer redeem a voucher owned by someone else
		if (!Objects.equals(voucher.getOwner(),redeemReq.getCustomerId())) {
			throw new ApiException(HttpStatus.UNAUTHORIZED,"voucher.unauthorized");
		}
	
		// find the marketer that issued the voucher
		Organization marketer = organizationService.findById(Long.valueOf(voucher.getIssuer()));
		if (marketer == null)
			throw new ApiException(HttpStatus.NOT_FOUND, "issuer.notFound");
		
		// obtain the merchant is an authorized merchant of the marketer
		AuthorizedMerchant am = merchantService.find(marketer, merchant);
		
		// find current merchant's top ancestor - we need this to support merchant chain
		Organization topAncestor = organizationService.findTopAncestor(merchant);
		
		// the merchant may not be in the authorized merchant list but its ancestor could be
		AuthorizedMerchant amAncestor = merchantService.find(marketer, topAncestor);
		
		// check if the merchant or its ancestor is an authorized merchant of any of the descendants of the marketer
		boolean existsInDescendants = merchantService.existsInDescendant(marketer, merchant)
				|| merchantService.existsInDescendant(marketer, topAncestor);
		
		Optional<Merchant> m = ValidationUtils.prepareMerchantFacts(merchant, topAncestor, 
				am, amAncestor, existsInDescendants);
		if (!m.isPresent())
			throw new ApiException(HttpStatus.FORBIDDEN, "merchant.notAuthorized");
		
		// set merchant
		redeemReq.setMerchant(m.get());
		// set order and payment
		if (req.getOrder() != null) {
			Map<String, Object> metadata = new HashMap<>();
			metadata.put("order", req.getOrder());
			if (req.getPaymentMethod() != null)
				metadata.put("payment", req.getPaymentMethod());
			redeemReq.setMetadata(metadata);
		}
		requestUser.setId(redeemReq.getReqUser());
		return redemptionClient.createRedemption(redeemReq);
	}
	
	@PutMapping
	public ConfirmRedemptionResponse confirm(@Valid @RequestBody CompleteRedemptionRequest req) {
		ConfirmRedemptionRequest confirmReq = new ConfirmRedemptionRequest();
		confirmReq.setVoucherId(req.getVoucherId());
		confirmReq.setOrderId(req.getOrderId());
		confirmReq.setStatus(req.getStatus());
		// re-create the merchant code to support CodeName.Code type
		Code mCode = CommonUtils.uniqueCode(requestUser.getAppOrg().getId().toString(), req.getMerchantCode());
		Organization merchant = organizationService.findByCode(mCode.getValue(), mCode.getName());
		confirmReq.setMerchantId(merchant.getId().toString());
		return redemptionClient.confirmRedemption(confirmReq);
	}
}
