package io.chainmind.myriad.domain.dto.voucher;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BatchTransferRequest {
	@NotBlank
	private String campaignId;
	// the organization that transfers vouchers
	@NotBlank
	private String reqOrgId;
	// the account that owns the vouchers
	@NotBlank
	private String reqUser;
	@NotEmpty
	private List<TransferDestination> entries = new ArrayList<TransferDestination>();

}
