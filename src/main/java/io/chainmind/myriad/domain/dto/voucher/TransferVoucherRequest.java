package io.chainmind.myriad.domain.dto.voucher;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferVoucherRequest {
	@NotBlank
	private String voucherId;
	
	// the user who transfer the voucher
	@NotBlank
	private String reqUser;
	
	// the user who will receive the voucher
	@NotBlank
	private String destUser;
}
