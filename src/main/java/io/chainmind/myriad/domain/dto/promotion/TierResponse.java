package io.chainmind.myriad.domain.dto.promotion;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

import io.chainmind.myriad.domain.dto.rule.RuleResponse;

@Getter
@Setter
public class TierResponse {
	private String name;
	private String description;
	
	// validation rule including actions for this tier
	private Set<RuleResponse> rules;
	
	private Map<String,Object> metadata;
}