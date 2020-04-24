package io.chainmind.myriad.domain.dto.distribution;

import java.util.Map;

import io.chainmind.myriad.domain.common.Channel;
import io.chainmind.myriad.domain.common.DistributionStatus;
import io.chainmind.myriad.domain.dto.CreateEntityResponse;
import io.chainmind.myriad.domain.dto.voucher.VoucherSummary;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DistributionResponse extends CreateEntityResponse {
	
	private VoucherSummary voucher;
	// the account that receives the voucher
	private String customerId;
	private String orgId;
	private Channel channel;
	// extra data
	private Map<String, Object> metadata;
	private DistributionStatus status;
	private String errorCode;
}
