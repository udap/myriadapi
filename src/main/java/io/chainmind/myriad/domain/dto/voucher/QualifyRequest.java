package io.chainmind.myriad.domain.dto.voucher;

import javax.validation.constraints.NotBlank;

import io.chainmind.myriad.domain.common.Order;
import io.chainmind.myriad.domain.common.VoucherType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QualifyRequest {
	// the customer
	@NotBlank
	private String customerId;
	
	// merchant where qualified vouchers will be redeemed for the order
	@NotBlank
	private String merchantId;
	
	// the order which qualified vouchers will be applied to
	private Order order;
	
	private VoucherType voucherType;

	private int limit;
}
