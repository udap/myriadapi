package io.chainmind.myriad.domain.dto.voucher.config;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeConfigCreationRequest {

	@Positive
	@Max(128)
	@Min(8)
	private Integer length;

	@Size(max = 64)
	private String charset;

	@Size(max = 10)
	private String prefix;

	@Size(max = 10)
	private String postfix;

	private String pattern;

}
