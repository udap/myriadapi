package io.chainmind.myriad.domain.dto.voucher;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Map;


@Getter
@Setter
public class AddVoucherToCampaignRequest {
	// voucher can override campaign.category
	private String category;

	@NotNull
	@Size(max = 32)
	private String code;

	// client may specify voucher metadata
	private Map<String, Object> metadata;

	

}
