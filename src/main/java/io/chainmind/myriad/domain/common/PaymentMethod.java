package io.chainmind.myriad.domain.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentMethod {
	private PaymentType type;
	// payment channel, e.g, amex, visa, mastercard, unionpay, bank name etc
	private String channel;
}
