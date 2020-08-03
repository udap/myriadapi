package io.chainmind.myriadapi.domain.dto;

import javax.validation.constraints.NotBlank;

import io.chainmind.myriad.domain.common.Order;
import io.chainmind.myriadapi.domain.CodeType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RedeemVoucherRequest {
	@NotBlank
	private String voucherId;
	@NotBlank
	private String reqUserId;
	@NotBlank
	private String merchantCode;
	@NotBlank
	private String orderId;
	// the issuer of the voucher to be redeemed
	private String issuerId;
	// the order that the voucher is redeemed against
	private Order order;
	
	private CodeType idType = CodeType.ID;
	private CodeType codeType = CodeType.ID;
}
