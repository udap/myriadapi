package io.chainmind.myriad.domain.dto.voucher;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BatchTransferRequest {
	@NotBlank
	private String reqOrgId;
	@NotEmpty
	private List<BatchTransfer> items;

}
