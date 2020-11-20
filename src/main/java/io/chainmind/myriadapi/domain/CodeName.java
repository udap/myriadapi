package io.chainmind.myriadapi.domain;

public enum CodeName {
	// common codes
	MIXED,
	ID, 
	NAME,
	// codes for account
	CELLPHONE, 
	EMAIL,
	SOURCE_ID, // external account id
	// codes for organization 
	CODE, 	// organization code
	LICENSE, 
	UPCODE, // unionpay merchant code
	WPCODE, // wechat pay merchant code
	APCODE; // alipay merchant code
	
	/**
	 * Returns an integer representing the meta type of the CodeName
	 * @return 0 for common type, 1 for account code type, 2 for organization code type
	 */
	public int getMetaType() {
		if (this.ordinal() > 5)
			return 2;
		else if (this.ordinal() > 2)
			return 1;
		else
			return 0;
	}
}
