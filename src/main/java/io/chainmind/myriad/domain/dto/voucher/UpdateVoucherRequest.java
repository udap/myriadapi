package io.chainmind.myriad.domain.dto.voucher;

import java.time.LocalDate;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateVoucherRequest {

	@NotBlank
	private String reqOrg;
	@NotBlank
	private String reqUser;

	@NotBlank
	private String id;

	@Size(max = 20)
	private String category;

	private LocalDate effective;

	private LocalDate expiry;

	private Map<String, Object> metadata;


}