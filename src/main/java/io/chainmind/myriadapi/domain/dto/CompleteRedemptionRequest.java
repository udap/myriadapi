package io.chainmind.myriadapi.domain.dto;

import javax.validation.constraints.NotBlank;

import io.chainmind.myriad.domain.common.RedemptionStatus;
import io.chainmind.myriadapi.domain.CodeType;
import lombok.Data;

@Data
public class CompleteRedemptionRequest {
	@NotBlank
	private String voucherId;
	@NotBlank
	private String merchantCode;
	@NotBlank
	private String orderId;
	@NotBlank
	private RedemptionStatus status;
	
	private CodeType codeType = CodeType.ID;
	
}
