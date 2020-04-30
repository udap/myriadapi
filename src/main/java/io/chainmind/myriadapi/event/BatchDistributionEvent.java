package io.chainmind.myriadapi.event;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.chainmind.myriad.domain.common.Channel;
import io.chainmind.myriadapi.domain.entity.Employee;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BatchDistributionEvent {
	
	private boolean all;
	
	// employee (as described by reqUser and reqOrg)
	@NotNull
	private Employee requestEmployee; 
	
	@NotBlank
	private String campaignId;
		
	private Channel channel;
	
	// extra data
	private Map<String, Object> metadata;
	
	private List<String> idList;

	
}
