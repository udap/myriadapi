package io.chainmind.myriad.domain.dto.campaign;

import io.chainmind.myriad.domain.common.PartyType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CampaignPartyResponse {

    private String partyId;

    private PartyType type;

    private Boolean approved;
}
