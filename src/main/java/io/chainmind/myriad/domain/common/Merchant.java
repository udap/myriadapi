package io.chainmind.myriad.domain.common;

import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Merchant {
	@NotBlank
	private String id;
	private String province;
	private String city;
	private String district;
	private String tags;
	private Merchant topAncestor;
}
