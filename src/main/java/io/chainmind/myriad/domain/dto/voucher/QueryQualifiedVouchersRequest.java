package io.chainmind.myriad.domain.dto.voucher;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.chainmind.myriad.domain.common.Merchant;
import io.chainmind.myriad.domain.common.Order;
import io.chainmind.myriad.domain.common.VoucherType;
import lombok.Data;

@Data
public class QueryQualifiedVouchersRequest {
	// the customer
	@NotBlank
	private String customerId;
	
	// merchant where qualified vouchers will be redeemed for the order
	@NotNull
	private Merchant merchant;
	
	// the order which qualified vouchers will be applied to
	private Order order;

	@NotNull
	private VoucherType type;

	@Min(value = 1)
	private int limit;
}
