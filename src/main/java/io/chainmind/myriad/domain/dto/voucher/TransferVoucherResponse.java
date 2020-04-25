package io.chainmind.myriad.domain.dto.voucher;

import io.chainmind.myriad.domain.common.TransferStatus;
import lombok.Data;

@Data
public class TransferVoucherResponse {
	// voucher id
	private String id;
	// the new owner of the voucher
	private String owner;
	// the status of the voucher
	private TransferStatus status;

}
