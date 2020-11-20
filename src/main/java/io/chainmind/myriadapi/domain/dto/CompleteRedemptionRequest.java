package io.chainmind.myriadapi.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.chainmind.myriad.domain.common.RedemptionStatus;
import io.chainmind.myriadapi.domain.CodeName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompleteRedemptionRequest {
	@NotBlank
	private String voucherId;
	@NotBlank
	private String merchantCode;
	@NotBlank
	private String orderId;
	@NotNull
	private RedemptionStatus status;
	
	private CodeName codeType = CodeName.ID;
	
}
