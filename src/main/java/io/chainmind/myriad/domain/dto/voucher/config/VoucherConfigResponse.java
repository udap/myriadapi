package io.chainmind.myriad.domain.dto.voucher.config;

import java.time.LocalDate;

import io.chainmind.myriad.domain.common.Product;
import io.chainmind.myriad.domain.common.VoucherType;
import io.chainmind.myriad.domain.dto.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoucherConfigResponse extends IdResponse {
	// voucher type
	private VoucherType type;

	private String name;
	private String description;
	
	private LocalDate effective;
	private LocalDate expiry;
	private Integer daysAfterDist;

	private Integer redemption;
	private Boolean authorizationRequired;
	// bulk code or fixed code
	private boolean multiple;
	// code for fixed code voucher config
	private String code;

	private String coverImg;
	private CodeConfigResponse codeConfig;

	private DiscountResponse discount;// COUPON

	// Gift
	private Product product;

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
