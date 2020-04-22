package io.chainmind.myriad.domain.dto.voucher;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateVoucherResponse {
	private String id;
	private String updatedBy;
	private LocalDateTime updatedAt;

}
