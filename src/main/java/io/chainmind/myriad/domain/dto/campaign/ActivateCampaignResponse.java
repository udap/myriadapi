package io.chainmind.myriad.domain.dto.campaign;

import io.chainmind.myriad.domain.common.CampaignStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ActivateCampaignResponse {
	// campaign identifier
    private String id;
    // campaign status
    private CampaignStatus status;
    
}
