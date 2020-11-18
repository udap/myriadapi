package io.chainmind.myriadapi.domain.dto;

import java.util.Map;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiCollectVoucherRequest {
	@NotBlank
	private String campaignId;
	
	@NotBlank
	private Code customer;
	
	// an optional voucher code
	private String voucherCode;

	private Map<String,Object> metadata;
}
