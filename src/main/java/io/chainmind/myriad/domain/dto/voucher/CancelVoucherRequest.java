package io.chainmind.myriad.domain.dto.voucher;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelVoucherRequest {
	@NotBlank
	private String id;
	@NotBlank
	private String reqUser;
	
}
