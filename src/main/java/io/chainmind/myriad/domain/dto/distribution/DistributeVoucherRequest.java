package io.chainmind.myriad.domain.dto.distribution;

import java.util.Map;

import javax.validation.constraints.NotBlank;

import io.chainmind.myriad.domain.common.Channel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DistributeVoucherRequest {
	// voucher to be distributed
	@NotBlank
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
	
	private Channel channel;
	// extra data
	private Map<String, Object> metadata;
}
