package io.chainmind.myriad.domain.dto.campaign;

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

	// total supply of vouchers

	/*@ApiModelProperty(value="生成券的最大数量")
	@Positive
	private Integer totalSupply;

	@ApiModelProperty(value="生成的券达到最大数量，是否可以增加")
	private Boolean autoUpdate;
*/



}
