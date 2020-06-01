package io.chainmind.myriad.domain.dto.voucher;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.chainmind.myriad.domain.common.Merchant;
import io.chainmind.myriad.domain.common.Order;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QualifyRequest {
	// voucher to be examined for redemption qualificaiton
	@NotBlank
	private String voucherId;
	
	// the customer
	@NotBlank
	private String customerId;
	
	// merchant where qualified vouchers will be redeemed for the order
	@NotNull
	private Merchant merchant;
	
	// the order which qualified vouchers will be applied to
	private Order order;

//	// the customer
//	@NotBlank
//	private String customerId;
//	
//	// merchant where qualified vouchers will be redeemed for the order
//	@NotBlank
//	private String merchantId;
//	
//	// the order which qualified vouchers will be applied to
//	private Order order;
//	
//	private VoucherType voucherType;
//
//	private int limit;
}
