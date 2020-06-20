package io.chainmind.myriad.domain.dto.distribution;

import io.chainmind.myriad.domain.common.DistributionStatus;
import io.chainmind.myriad.domain.dto.CreateEntityResponse;
import io.chainmind.myriad.domain.dto.voucher.VoucherSummary;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class DistributionResponse extends CreateEntityResponse {
	
	private VoucherSummary voucher;
	private String fromOwner;
	// the account that receives the voucher
	private String customerId;
	private String orgId;
	private String channel;
	// extra data
	private Map<String, Object> metadata;
	private DistributionStatus status;
	private String errorCode;
}
