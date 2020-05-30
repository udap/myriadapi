package io.chainmind.myriadapi.domain;

public enum CodeType {
	ID, 
	CELLPHONE, 
	EMAIL,
	// external account id
	SOURCE_ID,
	LICENSE, 
	UPCODE, // unionpay merchant code
	WPCODE, // wechat pay merchant code
	APCODE; // alipay merchant code
}
