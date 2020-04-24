package io.chainmind.myriad.domain.dto.voucher.config;

import io.chainmind.myriad.domain.common.VoucherType;
import io.chainmind.myriad.domain.dto.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoucherConfigResponse extends IdResponse {
	// voucher type
	private VoucherType type;

	//private String name;
	private String description;
	private Integer redemption;
	private Boolean authorizationRequired;
	// bulk code or fixed code
	private boolean multiple;
	// code for fixed code voucher config
	private String code;
	// number of vouchers to be issued
	private int totalSupply;
	// allow or disallow additional issuance
	private boolean autoUpdate;

	private String coverImg;
	private CodeConfigResponse codeConfig;

	private DiscountResponse discount;// COUPON

	private String productCode;// GIFT

	private Integer amount;// PREPAID_CARD
	private CurrencyResponse currency;// PREPAID_CARD or LOYALTY_CARD

	/// symbol of the points, e.g., 'WALMART COIN'
	private String symbol; // LOYALTY_CARD
	/// exchange rate per currency unit, e.g., if exchangeRate is 100 points per
	/// Currency.USD unit,
	/// then 1000 points equals 10 US Dollars; exchangeRate and currency are
	/// optional attributes
//	private Integer exchangeRate; // LOYALTY_CARD
//
//	private CustomerResponse customer; // Membership
//	// total points earned
//	private Integer points; // Membership
//	// balance points
//	private Integer balance; // Membership

}
