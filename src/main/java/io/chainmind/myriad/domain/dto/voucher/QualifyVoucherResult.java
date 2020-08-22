package io.chainmind.myriad.domain.dto.voucher;

import lombok.Data;

@Data
public class QualifyVoucherResult {
	private String voucherId;
	private boolean qualified;
}
