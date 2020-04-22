package io.chainmind.myriad.domain.dto.redemption;

import java.util.Map;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRedemptionRequest {
	@NotBlank
	private String voucherId;
	
	// the account that redeems the voucher
	@NotBlank
	private String reqUser;
	// the merchant that redeems the voucher
	@NotBlank
	private String merchantId;
	
	@NotBlank
	private String orderId;
	
	// customer that redeems the voucher
	@NotBlank
	private String customerId;

	// extra data
	private Map<String, Object> metadata;

}
