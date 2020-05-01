package io.chainmind.myriadapi.domain.dto;

import java.util.Map;

import javax.validation.constraints.NotBlank;

import io.chainmind.myriad.domain.common.Channel;
import io.chainmind.myriadapi.domain.CodeType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DistributeToSingleCustomerRequest {
	// distribution mode, default to ANY
	private DistributionMode strictMode = DistributionMode.ANY;
	
	// campaign id. required for auto distribution
	private String campaignId;
	
	// voucher to be distributed
	// if this is not provided, system will choose a voucher for distribution
	private String voucherId;
	
	// the organization that distributes the voucher
	@NotBlank
	private String reqOrg;
	// the account that distributes the voucher
	@NotBlank
	private String reqUser;
	// the account that receives the voucher
	@NotBlank
	private String customerId;
	
	private CodeType customerIdType = CodeType.ID;
	
	private Channel channel = Channel.API;
	// extra data
	private Map<String, Object> metadata;

}
