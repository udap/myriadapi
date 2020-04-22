package io.chainmind.myriad.domain.dto.redemption;

import io.chainmind.myriad.domain.common.RedemptionStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConfirmRedemptionResponse {
	// redemption identifier
    private String id;
    // voucher identifier
    private String voucherId;
    // most recent redemption status
    private RedemptionStatus status;
}
