package io.chainmind.myriadapi.domain.dto;

import javax.validation.constraints.NotNull;

import io.chainmind.myriad.domain.common.Order;
import lombok.Data;

@Data
public class ApiQualifyCouponsRequest {
	@NotNull
	private Code accountCode;
	@NotNull
	private Code merchantCode;
	
	// the order which qualified vouchers will be applied to
	private Order order;
	@NotNull
	private Integer limit = 100;

}
