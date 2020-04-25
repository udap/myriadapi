package io.chainmind.myriad.domain.dto.distribution;

import io.chainmind.myriad.domain.common.DistributionStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DistributeVoucherResponse {
	// voucher id
	private String id;
	// the new owner of the voucher
	private String owner;
	// the status of the voucher
	private DistributionStatus status;

}
