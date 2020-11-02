package io.chainmind.myriad.domain.dto.distribution;

import java.time.LocalDateTime;

import io.chainmind.myriad.domain.common.DistributionStatus;
import io.chainmind.myriad.domain.dto.campaign.NamedCampaign;
import io.chainmind.myriad.domain.dto.voucher.SimpleVoucherItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DistributionListItem {

	private String id;
	private SimpleVoucherItem voucher;
	private NamedCampaign campaign;
	private String orgId;
	private String fromOwner;
	// customer whom the voucher is distributed to
	private String customerId;
	// which channel is used to distribute the voucher
	private String channel;
	// distribution status
	private DistributionStatus status;
	private LocalDateTime updatedAt;
	// error code if distribution fails
	private String errorCode;

	// coupon voucher
	private String discountType;
	// discount off
	private Integer discountOff;
	// gift voucher
	private String productName;
	private String productCode;
	private Long productMarketPrice;
	private Long productExchangePrice;


}
