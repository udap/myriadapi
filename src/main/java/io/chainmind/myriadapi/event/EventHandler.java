package io.chainmind.myriadapi.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import io.chainmind.myriadapi.service.DistributionService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EventHandler {
	@Autowired
	private DistributionService distributionService;
	
    @Async
    @EventListener
	public void handleBatchDistribution(BatchDistributionEvent event) {
        log.info("Distributing vouchers in batch...",event);
        distributionService.distributeVouchers(event);
        log.info("Distributing vouchers...",event);
	}
}
