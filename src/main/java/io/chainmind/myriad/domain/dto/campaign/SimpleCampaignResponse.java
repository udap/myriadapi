package io.chainmind.myriad.domain.dto.campaign;

import io.chainmind.myriad.domain.dto.CreateEntityResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleCampaignResponse extends CreateEntityResponse {
	private String name;
}
