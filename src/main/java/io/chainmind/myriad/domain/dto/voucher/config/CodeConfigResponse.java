package io.chainmind.myriad.domain.dto.voucher.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeConfigResponse {

	private Integer length;

	private String charset;

	private String prefix;

	private String postfix;

	private String pattern;

}
