package io.chainmind.myriadapi.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import io.chainmind.myriad.domain.dto.distribution.BatchDistributionResponse;
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
        log.info("Start processing batch distribution event...{}",event);
        try {
        	BatchDistributionResponse resp = distributionService.distributeVouchers(event);
        	log.info("Batch distribution response...{}", resp.toString());
        	// TODO: consider using websocket to send a notification to the reqUser
        	
        } catch(Exception e) {
        	log.error("Failed processing batch distribution event", e);
        }
        log.info("Complete processing batch distribution event...");
	}
}
