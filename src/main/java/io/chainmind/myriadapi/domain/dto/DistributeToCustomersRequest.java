package io.chainmind.myriadapi.domain.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import io.chainmind.myriad.domain.common.Channel;
import io.chainmind.myriadapi.domain.CodeType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DistributeToCustomersRequest {
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

	// distribute to request user's customers only (strict mode) 
	// or distribute to request org's customer only
	private DistributionMode strictMode = DistributionMode.ANY;
	
	// distribute to all customers? if this property is set to true, the customers list
	// will be ignored
	private boolean all = false;
	
	private List<String> accounts = new ArrayList<String>();

	private CodeType accountCodeType = CodeType.ID;
	
}
