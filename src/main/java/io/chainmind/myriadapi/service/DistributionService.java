package io.chainmind.myriadapi.service;

import io.chainmind.myriad.domain.dto.distribution.BatchDistributionResponse;
import io.chainmind.myriadapi.event.BatchDistributionEvent;

public interface DistributionService {
	BatchDistributionResponse distributeVouchers(BatchDistributionEvent event);
}
