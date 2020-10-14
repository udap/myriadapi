package io.chainmind.myriadapi.domain.dto;

import io.chainmind.myriad.domain.common.Channel;
import io.chainmind.myriadapi.domain.CodeType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Getter
@Setter
public class ApiCollectVoucherRequest {
	@NotBlank
	private String campaignId;
	@NotBlank
	private String customerId;

	private CodeType customerIdType = CodeType.ID;
	private Channel channel = Channel.API;
	private Map<String,Object> metadata;
}
