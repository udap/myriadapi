package io.chainmind.myriad.domain.dto.campaign;

import io.chainmind.myriad.domain.dto.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NamedCampaign extends IdResponse {
	private String name;

}
