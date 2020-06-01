package io.chainmind.myriad.domain.dto.voucher;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QualifyResult {
	private String voucherId;
	private boolean qualified;
}
