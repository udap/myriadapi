package io.chainmind.myriad.domain.dto.voucher;

import lombok.Data;

@Data
public class QualifyVoucherResponse {
	private String voucherId;
	private boolean qualified;
}
