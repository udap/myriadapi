package io.chainmind.myriad.domain.dto.redemption;

import io.chainmind.myriad.domain.common.RedemptionStatus;
import io.chainmind.myriad.domain.dto.CreateEntityResponse;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class CreateRedemptionResponse extends CreateEntityResponse {
    // most recent redemption status
    private RedemptionStatus status;

}
