package io.chainmind.myriad.domain.dto.campaign;

import javax.validation.constraints.Positive;

import io.chainmind.myriad.domain.common.CampaignType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateVoucherCampaignRequest extends CreateCampaignRequest {
	private static final long serialVersionUID = -4133242945397511653L;

	public CreateVoucherCampaignRequest() {
		this.setType(CampaignType.VOUCHER);
	}

	@Positive
	private int totalSupply;

	private boolean autoUpdate;

}
