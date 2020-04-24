package io.chainmind.myriad.domain.dto.redemption;

import io.chainmind.myriad.domain.common.RedemptionStatus;
import io.chainmind.myriad.domain.dto.CreateEntityResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRedemptionResponse extends CreateEntityResponse {
    // most recent redemption status
    private RedemptionStatus status;

}
