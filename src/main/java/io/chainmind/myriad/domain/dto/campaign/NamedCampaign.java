package io.chainmind.myriad.domain.dto.campaign;

import io.chainmind.myriad.domain.dto.IdResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class NamedCampaign extends IdResponse {
	private String name;

}
