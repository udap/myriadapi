package io.chainmind.myriad.domain.dto.voucher.config;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.chainmind.myriad.domain.common.DiscountType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnitDiscountCreationRequest extends DiscountCreationRequest {

	private static final long serialVersionUID = 8506878802222448539L;

	public UnitDiscountCreationRequest() {
		this.setType(DiscountType.UNIT);
	}

	@NotNull
	@Min(1)
	private Integer valueOff;

	@Size(max = 20)
	@NotBlank
	private String unitType;

}
