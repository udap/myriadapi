package io.chainmind.myriad.domain.dto.voucher;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BatchTransfer {
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
