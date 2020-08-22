package io.chainmind.myriad.domain.dto.voucher;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.chainmind.myriad.domain.common.Merchant;
import io.chainmind.myriad.domain.common.Order;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QualifyVoucherRequest {
	@NotBlank
	private String id;
	// merchant where qualified vouchers will be redeemed for the order
	@NotNull
	private Merchant merchant;
	
	// the order which qualified vouchers will be applied to
	private Order order;

}
