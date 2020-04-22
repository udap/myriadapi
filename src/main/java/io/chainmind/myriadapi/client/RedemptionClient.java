package io.chainmind.myriadapi.client;

import javax.validation.Valid;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.chainmind.myriad.domain.dto.redemption.ConfirmRedemptionRequest;
import io.chainmind.myriad.domain.dto.redemption.ConfirmRedemptionResponse;
import io.chainmind.myriad.domain.dto.redemption.CreateRedemptionRequest;
import io.chainmind.myriad.domain.dto.redemption.CreateRedemptionResponse;

@FeignClient(name = "voucher-service",url="${myriad.ribbon.listOfServers}")
public interface RedemptionClient {
	@PostMapping
	CreateRedemptionResponse create(@Valid @RequestBody CreateRedemptionRequest req);

	@PutMapping
	public ConfirmRedemptionResponse confirm(@Valid @RequestBody ConfirmRedemptionRequest req);

}
