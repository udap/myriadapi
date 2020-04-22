package io.chainmind.myriad.domain.dto.campaign;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.chainmind.myriad.domain.common.PartyType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCampaignPartyRequest {

    @NotBlank
    private String reqUser;

    @NotNull
    private String partyId;

    @NotNull
    private PartyType type;


}
