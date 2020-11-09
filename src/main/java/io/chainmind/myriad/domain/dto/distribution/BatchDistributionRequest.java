package io.chainmind.myriad.domain.dto.distribution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import io.chainmind.myriad.domain.common.Audience;
import io.chainmind.myriad.domain.common.Channel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BatchDistributionRequest {
	@NotBlank
	private String campaignId;
	
	@NotBlank
	private String reqOrg;
	// the account that distributes the voucher
	@NotBlank
	private String reqUser;
	
	private Channel channel = Channel.API;
	// extra data
	private Map<String, Object> metadata;

	private Map<String,String>  departmentIds;
	
	@NotEmpty
	private List<Audience> customers = new ArrayList<>();

}
