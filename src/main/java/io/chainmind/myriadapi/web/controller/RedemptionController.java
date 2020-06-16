package io.chainmind.myriadapi.web.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
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
import io.chainmind.myriadapi.client.VoucherClient;
import io.chainmind.myriadapi.domain.RequestUser;
import io.chainmind.myriadapi.domain.dto.CompleteRedemptionRequest;
import io.chainmind.myriadapi.domain.dto.RedeemVoucherRequest;
import io.chainmind.myriadapi.domain.entity.Account;
import io.chainmind.myriadapi.domain.entity.AuthorizedMerchant;
import io.chainmind.myriadapi.domain.entity.Organization;
import io.chainmind.myriadapi.domain.exception.ApiException;
import io.chainmind.myriadapi.service.AccountService;
import io.chainmind.myriadapi.service.AuthorizedMerchantService;
import io.chainmind.myriadapi.service.OrganizationService;

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
	@Autowired
	private RequestUser requestOrg;
	@Autowired
	private RequestUser requestUser;

	@PostMapping
	public CreateRedemptionResponse create(@Valid @RequestBody RedeemVoucherRequest req) {
		CreateRedemptionRequest redeemReq = new CreateRedemptionRequest();
		redeemReq.setOrderId(req.getOrderId());
		redeemReq.setVoucherId(req.getVoucherId());
		// query account
		Account account = accountService.findByCode(req.getReqUserId(), req.getIdType());
		if (account == null)
			throw new ApiException(HttpStatus.NOT_FOUND, "request user not found");
		redeemReq.setCustomerId(account.getId().toString());
		redeemReq.setReqUser(redeemReq.getCustomerId());
		// query merchant
		Organization merchant = organizationService.findByCode(req.getMerchantCode(), req.getCodeType());
		Organization marketer = requestOrg.getAppOrg();
		if (StringUtils.hasText(req.getIssuerId()))
			marketer = organizationService.findById(Long.valueOf(req.getIssuerId()));
		// find current merchant's top ancestor - we need this to support merchant chain
		Organization topAncestor = organizationService.findTopAncestor(merchant);
		AuthorizedMerchant am = merchantService.find(marketer, merchant);
		
		// the merchant may not be in the authorized merchant list but its ancestor could be
		AuthorizedMerchant amAncestor = merchantService.find(marketer, topAncestor);

		if (Objects.isNull(am) && Objects.isNull(amAncestor))
			throw new ApiException(HttpStatus.FORBIDDEN, "merchant.notAuthorized");
		
		Merchant mAncestor = null;
		if (topAncestor.getId() != merchant.getId()) {
			mAncestor = Merchant.builder()
					.id(topAncestor.getId().toString())
					.tags(amAncestor.getTags())
					.build();			
		}
		
		redeemReq.setMerchant(Merchant.builder()
				.id(merchant.getId().toString())
				.province(merchant.getProvince())
				.city(merchant.getCity())
				.district(merchant.getDistrict())
				.tags(Objects.nonNull(am)?am.getTags():null)
				.topAncestor(mAncestor)
				.build());
		if (req.getOrder() != null) {
			Map<String, Object> metadata = new HashMap<>();
			metadata.put("order", req.getOrder());
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
		Organization merchant = organizationService.findByCode(req.getMerchantCode(), req.getCodeType());
		confirmReq.setMerchantId(merchant.getId().toString());
		return redemptionClient.confirmRedemption(confirmReq);
	}
}
