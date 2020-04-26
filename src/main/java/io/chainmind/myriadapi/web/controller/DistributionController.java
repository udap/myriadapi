package io.chainmind.myriadapi.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.chainmind.myriad.domain.dto.distribution.BatchDistributionResponse;
import io.chainmind.myriad.domain.dto.distribution.DistributeVoucherRequest;
import io.chainmind.myriad.domain.dto.distribution.DistributeVoucherResponse;
import io.chainmind.myriadapi.client.VoucherClient;
import io.chainmind.myriadapi.domain.CodeType;
import io.chainmind.myriadapi.domain.dto.DistributeToCustomersRequest;
import io.chainmind.myriadapi.domain.entity.Account;
import io.chainmind.myriadapi.service.AccountService;

@RestController
@RequestMapping("/api/distributions")
public class DistributionController {
	@Autowired
	private VoucherClient voucherClient;
	
	@Autowired
	private AccountService accountService;

	@PostMapping
	public DistributeVoucherResponse distributeVoucher(@Valid @RequestBody DistributeVoucherRequest req) {
		return voucherClient.distributeVoucher(req);
	}
	
	@PostMapping("/batch")
	public BatchDistributionResponse distributeVouchers(@Valid @RequestBody DistributeToCustomersRequest req) {
		Map<Long, String> idMap = new HashMap<Long,String>();
		// ensure a valid code type for customer
		if (CodeType.CELLPHONE.equals(req.getCustomerCodeType()) ||
				CodeType.EMAIL.equals(req.getCustomerCodeType()) ||
				CodeType.ID.equals(req.getCustomerCodeType())) {
		
			for (String code: req.getCustomers()) {
				Account account = accountService.findByCode(code, req.getCustomerCodeType());
				if (account != null) {
					idMap.putIfAbsent(account.getId(), (account.getId().toString()));
				}
			}
			// replace with clean data
			req.replaceCustomerCodesWith(idMap.values());
			return voucherClient.distributeVouchers(req);
		} else {
			BatchDistributionResponse response = new BatchDistributionResponse();
			response.setCustomerCount(req.getCustomers().size());
			return response;
		}
		
	}
}
