package io.chainmind.myriad.domain.dto.distribution;

import io.chainmind.myriad.domain.common.DistributionStatus;
import io.chainmind.myriad.domain.dto.campaign.SimpleCampaignItem;
import io.chainmind.myriad.domain.dto.voucher.SimpleVoucherItem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DistributionListItem {
	private String id;
	private SimpleVoucherItem voucher;
	private SimpleCampaignItem campaign;
	// customer whom the voucher is distributed to
	private String customerId;
	// which channel is used to distribute the voucher
	private String channel;
	// distribution status
	private DistributionStatus status;
	private LocalDateTime updatedAt;
	// error code if distribution fails
	private String errorCode;	
}
