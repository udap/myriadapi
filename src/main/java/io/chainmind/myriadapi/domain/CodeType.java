package io.chainmind.myriadapi.domain;

public enum CodeType {
	ID, 
	CELLPHONE, 
	EMAIL,
	LICENSE, 
	UPCODE, // unionpay merchant code
	WPCODE, // wechat pay merchant code
	APCODE; // alipay merchant code
}
