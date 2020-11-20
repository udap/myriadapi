package io.chainmind.myriad.domain.dto.voucher.config;

import io.chainmind.myriad.domain.common.Product;
import io.chainmind.myriad.domain.common.VoucherType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleVoucherConfig {
	// voucher type
	private VoucherType type;

	private String name;
	private String description;
	
	private Integer redemption;
	private Boolean authorizationRequired;
	
	// effective days after distribution
	private Integer daysAfterDist;

	private String coverImg;

	private DiscountResponse discount;// COUPON

	private Product product; // GIFT

}
