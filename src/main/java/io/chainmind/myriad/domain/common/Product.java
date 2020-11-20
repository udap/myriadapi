package io.chainmind.myriad.domain.common;

import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {
	@NotBlank
	private String name;
	// SKU
	@NotBlank
	private String code;
	private Integer marketPrice;
	@NotBlank
	private Integer exchangePrice;
}
