package io.chainmind.myriad.domain.dto.voucher;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class BatchTransferRequest {

	@NotBlank
	private String campaignId;
	@NotBlank
	private String reqUser;
	@NotBlank
	private String fromOwner;
	@NotBlank
	private String toOwner;
	@Positive
	private int amount;
}
