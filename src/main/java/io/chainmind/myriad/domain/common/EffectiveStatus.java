package io.chainmind.myriad.domain.common;

import java.time.LocalDate;

public enum EffectiveStatus {
	// campaign is activated but not started
	PENDING,
	// campaign is activated and in progress
	ACTIVE,
	// pending and active
	NON_EXPIRED,
	// campaign is activated but expired
	EXPIRED;

	public static EffectiveStatus getStatus(LocalDate effective, LocalDate expiry) {
		if (expiry != null && !expiry.isAfter(LocalDate.now())){
			return EXPIRED;
		}else if (effective.isAfter(LocalDate.now())){
			return PENDING;
		}else{
			return ACTIVE;
		}
	}

}
