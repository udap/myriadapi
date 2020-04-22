package io.chainmind.myriad.domain.dto.voucher;

import java.time.LocalDateTime;

import io.chainmind.myriad.domain.common.VoucherStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CancelVoucherResponse {
	private String id;
	private VoucherStatus status;
	private LocalDateTime updatedAt;
	private String updatedBy;
}
