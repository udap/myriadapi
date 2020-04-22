package io.chainmind.myriadapi.domain.dto;

import javax.validation.constraints.NotBlank;

import io.chainmind.myriadapi.domain.CodeType;
import lombok.Data;

@Data
public class RedeemVoucherRequest {
	@NotBlank
	private String voucherId;
	@NotBlank
	private String reqUserId;
	@NotBlank
	private String merchantCode;
	@NotBlank
	private String orderId;
	
	private CodeType idType;
	private CodeType codeType;
}
