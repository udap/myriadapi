package io.chainmind.myriad.domain.dto.promotion;

import java.util.Map;
import java.util.Set;

import io.chainmind.myriad.domain.dto.rule.ValidationDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TierResponse {
	private String name;
	private String description;
	
	// validation rule including actions for this tier
	private Set<ValidationDTO> rules;
	
	private Map<String,Object> metadata;
}