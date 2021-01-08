package io.chainmind.myriadapi.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.chainmind.myriad.domain.common.Order;
import io.chainmind.myriad.domain.common.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RedeemVoucherRequest {
	@NotBlank
	private String voucherId;
	@NotNull
	private Code accountCode;
	@NotNull
	private Code merchantCode;
	// the order that the voucher is redeemed against
	@NotNull
	private Order order;
	// an optional payment method indicating how this order will be paid
	private PaymentMethod paymentMethod;
}
