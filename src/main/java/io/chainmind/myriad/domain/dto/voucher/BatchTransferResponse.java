package io.chainmind.myriad.domain.dto.voucher;

import io.chainmind.myriad.domain.common.TransferStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BatchTransferResponse {
	private Long batchId;
	private int requestedAmount;
	private TransferStatus status;
}
