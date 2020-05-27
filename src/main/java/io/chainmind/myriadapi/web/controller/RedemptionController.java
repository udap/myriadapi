package io.chainmind.myriadapi.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.chainmind.myriad.domain.dto.redemption.ConfirmRedemptionRequest;
import io.chainmind.myriad.domain.dto.redemption.ConfirmRedemptionResponse;
import io.chainmind.myriad.domain.dto.redemption.CreateRedemptionRequest;
import io.chainmind.myriad.domain.dto.redemption.CreateRedemptionResponse;
import io.chainmind.myriadapi.client.VoucherClient;
import io.chainmind.myriadapi.domain.dto.CompleteRedemptionRequest;
import io.chainmind.myriadapi.domain.dto.RedeemVoucherRequest;
import io.chainmind.myriadapi.domain.entity.Account;
import io.chainmind.myriadapi.domain.exception.ApiException;
import io.chainmind.myriadapi.service.AccountService;
import io.chainmind.myriadapi.service.AuthorizedMerchantService;

@RestController
@RequestMapping("/api/redemptions")
public class RedemptionController {
	
	@Autowired
	private VoucherClient redemptionClient;
	@Autowired
	private AccountService accountService;
	@Autowired
	private AuthorizedMerchantService merchantService;

	@PostMapping
	public CreateRedemptionResponse create(@Valid @RequestBody RedeemVoucherRequest req) {
		merchantService.getId(req.getMerchantCode(), req.getCodeType());
		
		CreateRedemptionRequest redeemReq = new CreateRedemptionRequest();
		redeemReq.setOrderId(req.getOrderId());
		redeemReq.setVoucherId(req.getVoucherId());
		redeemReq.setMerchantId(merchantService.getId(req.getMerchantCode(), req.getCodeType()));
		Account account = accountService.findByCode(req.getReqUserId(), req.getIdType());
		if (account == null)
			throw new ApiException(HttpStatus.NOT_FOUND, "request user not found");
		redeemReq.setCustomerId(account.getId().toString());
		redeemReq.setReqUser(redeemReq.getCustomerId());
		if (req.getOrder() != null) {
			Map<String, Object> metadata = new HashMap<>();
			metadata.put("order", req.getOrder());
			redeemReq.setMetadata(metadata);
		}
		return redemptionClient.createRedemption(redeemReq);
	}
	
	@PutMapping
	public ConfirmRedemptionResponse confirm(@Valid @RequestBody CompleteRedemptionRequest req) {
		ConfirmRedemptionRequest confirmReq = new ConfirmRedemptionRequest();
		confirmReq.setVoucherId(req.getVoucherId());
		confirmReq.setOrderId(req.getOrderId());
		confirmReq.setStatus(req.getStatus());
		confirmReq.setMerchantId(merchantService.getId(req.getMerchantCode(), req.getCodeType()));
		return redemptionClient.confirmRedemption(confirmReq);
	}
}
