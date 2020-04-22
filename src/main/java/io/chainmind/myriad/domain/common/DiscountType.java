package io.chainmind.myriad.domain.common;

public enum DiscountType {
	AMOUNT, PERCENT, UNIT;

	public static class Constants {
		public static final String AMOUNT = "AMOUNT";
		public static final String PERCENT = "PERCENT";
		public static final String UNIT = "UNIT";
	}
}
