package io.chainmind.myriad.domain.dto.voucher.config;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.chainmind.myriad.domain.common.DiscountType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PercentDiscountCreationRequest extends DiscountCreationRequest {

	private static final long serialVersionUID = -4384977095360831596L;

	public PercentDiscountCreationRequest() {
		this.setType(DiscountType.PERCENT);
	}

	@NotNull
	@Min(1)
	@Max(100)
	private Integer valueOff;

}
