package io.chainmind.myriad.domain.dto.redemption;

import io.chainmind.myriad.domain.common.RedemptionStatus;
import io.chainmind.myriad.domain.dto.CreateEntityResponse;
import io.chainmind.myriad.domain.dto.campaign.SimpleCampaignResponse;
import io.chainmind.myriad.domain.dto.voucher.SimpleVoucherResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RedemptionListItemResponse extends CreateEntityResponse {
	private SimpleVoucherResponse voucher;
	private SimpleCampaignResponse campaign;
	// customer whom the voucher is distributed to
	private String customerId;
	
	// organization that redeems the voucher
	private String merchantId;
	
	private RedemptionStatus status;
	private String errorCode;
}
