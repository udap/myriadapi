package io.chainmind.myriad.domain.dto.distribution;

import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.chainmind.myriad.domain.common.Audience;
import io.chainmind.myriad.domain.common.Channel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DistributeVoucherRequest {
	// campaign id. Caller must provide for auto distribution
	private String campaignId;
	// voucher to be distributed. null means auto distribution
	private String voucherId;
	// the organization that distributes the voucher
	@NotBlank
	private String reqOrg;
	// the account that distributes the voucher
	@NotBlank
	private String reqUser;
	// the account that receives the voucher
	@NotNull
	private Audience audience;

	private String channel = Channel.API.name();
	// extra data
	private Map<String, Object> metadata;
}
