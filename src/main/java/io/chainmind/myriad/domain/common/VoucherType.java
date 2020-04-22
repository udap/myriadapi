package io.chainmind.myriad.domain.common;

public enum VoucherType {
	COUPON, GIFT, PREPAID_CARD, LOYALTY_CARD;

	public static class Constants {
		public static final String COUPON = "COUPON";
		public static final String GIFT = "GIFT";
		public static final String PREPAID_CARD = "PREPAID_CARD";
		public static final String LOYALTY_CARD = "LOYALTY_CARD";
	}

}
