package io.chainmind.myriad.domain.dto.distribution;

import io.chainmind.myriad.domain.common.Channel;
import io.chainmind.myriad.domain.common.DistributionStatus;
import io.chainmind.myriad.domain.dto.CreateEntityResponse;
import io.chainmind.myriad.domain.dto.campaign.SimpleCampaignResponse;
import io.chainmind.myriad.domain.dto.voucher.SimpleVoucherResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DistributionListItemResponse extends CreateEntityResponse {
	private SimpleVoucherResponse voucher;
	private SimpleCampaignResponse campaign;
	// customer whom the voucher is distributed to
	private String customerId;
	// which channel is used to distribute the voucher
	private Channel channel;
	// distribution status
	private DistributionStatus status;
	// error code if distribution fails
	private String errorCode;	
}
