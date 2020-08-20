package io.chainmind.myriad.domain.dto.distribution;

import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.chainmind.myriad.domain.common.Audience;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CollectVoucherRequest {
	@NotBlank
	private String campaignId;
	@NotNull
	private Audience audience;
	@NotBlank
	private String channel;
	private Map<String,Object> metadata;
}
