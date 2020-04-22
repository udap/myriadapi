package io.chainmind.myriad.domain.dto.voucher.config;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.chainmind.myriad.domain.common.DiscountType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AmountDiscountCreationRequest extends DiscountCreationRequest {

	private static final long serialVersionUID = -4384977095360831596L;

	public AmountDiscountCreationRequest() {
		this.setType(DiscountType.AMOUNT);
	}

	@NotNull
	@Min(1)
	private Integer valueOff;

	@NotNull
	@Min(1)
	private Integer amountLimit;

}
