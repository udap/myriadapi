package io.chainmind.myriad.domain.dto.voucher;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiscountRequest {
	@NotNull
	private String type;
	@Positive
	private Integer amountOff;
	@Positive
	@Max(100)
	private Double percentOff;
	@Positive
	private Integer amountLimit;
	@Positive
	private Integer unitOff;
	@Size(max = 20)
	private String unitType;

}
