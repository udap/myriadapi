package io.chainmind.myriad.domain.dto.voucher;

public enum UsageStatus {
	NEW,
	PENDING, // distributed but not effective 
	ACTIVE,  // distributed and effective
	REDEEMED,// distributed and redeemed or redeeming 
	EXPIRED  // distributed but expired
}
