package io.chainmind.myriadapi.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.chainmind.myriad.domain.dto.distribution.DistributeVoucherRequest;
import io.chainmind.myriad.domain.dto.distribution.DistributeVoucherResponse;
import io.chainmind.myriadapi.client.VoucherClient;

@RestController
@RequestMapping("/api/distributions")
public class DistributionController {
	@Autowired
	private VoucherClient voucherClient;

	@PostMapping
	public DistributeVoucherResponse distributeVoucher(@Valid @RequestBody DistributeVoucherRequest req) {
		return voucherClient.distributeVoucher(req);
	}
}
