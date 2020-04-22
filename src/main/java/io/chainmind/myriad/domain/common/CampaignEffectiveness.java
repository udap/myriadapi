package io.chainmind.myriad.domain.common;

import java.time.LocalDate;

public enum CampaignEffectiveness {
	// campaign is activated and in progress
	ACTIVE, 
	// campaign is activated but not started
	PENDING, 
	// campaign is activated but expired
	EXPIRED,
	// campaign is neither activated not started 
	DRAFT,
	// campaign is not activated (but started) or is terminated/archived/rejected/draft
	INVALID;

	public static CampaignEffectiveness of(CampaignStatus status, LocalDate effective, LocalDate expiry) {
		if (!status.equals(CampaignStatus.ACTIVATED) && !status.equals(CampaignStatus.ACTIVATING)) {
			if (effective.isAfter(LocalDate.now()) && status.equals(CampaignStatus.INITIATED))
				return DRAFT;
			return INVALID;
		}
		// if campaign is activating or activated, check expiration
		if (expiry != null && expiry.isBefore(LocalDate.now()))
			return EXPIRED;
		else if (effective.isAfter(LocalDate.now()))
			return PENDING;
		else
			return ACTIVE;
	}

}