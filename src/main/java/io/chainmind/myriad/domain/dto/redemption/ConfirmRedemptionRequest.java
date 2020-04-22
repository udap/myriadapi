package io.chainmind.myriad.domain.dto.redemption;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.chainmind.myriad.domain.common.RedemptionStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmRedemptionRequest {
	@NotBlank
	private String voucherId;
	@NotBlank
	private String merchantId;
	@NotBlank
	private String orderId;
	@NotNull
	private RedemptionStatus status;
}
