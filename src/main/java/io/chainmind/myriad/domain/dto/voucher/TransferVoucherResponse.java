package io.chainmind.myriad.domain.dto.voucher;

import io.chainmind.myriad.domain.common.VoucherStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TransferVoucherResponse {
	// voucher id
	private String id;
	// the new owner of the voucher
	private String owner;
	// the status of the voucher
	private VoucherStatus status;

}
