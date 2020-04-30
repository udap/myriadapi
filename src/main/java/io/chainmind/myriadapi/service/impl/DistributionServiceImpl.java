package io.chainmind.myriadapi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import io.chainmind.myriad.domain.dto.distribution.BatchDistributionRequest;
import io.chainmind.myriad.domain.dto.distribution.BatchDistributionResponse;
import io.chainmind.myriadapi.client.VoucherClient;
import io.chainmind.myriadapi.domain.entity.Customer;
import io.chainmind.myriadapi.event.BatchDistributionEvent;
import io.chainmind.myriadapi.service.CustomerService;
import io.chainmind.myriadapi.service.DistributionService;

@Service
public class DistributionServiceImpl implements DistributionService {

	@Autowired
	private VoucherClient voucherClient;
	@Autowired
	private CustomerService customerService;
	
	@Override
	public BatchDistributionResponse distributeVouchers(BatchDistributionEvent event) {		
		if (!event.isAll()) {	
			BatchDistributionRequest req = new BatchDistributionRequest();
			req.setCampaignId(event.getCampaignId());
			req.setChannel(event.getChannel());
			req.setMetadata(req.getMetadata());
			req.setReqOrg(event.getRequestEmployee().getOrg().getId().toString());
			req.setReqUser(event.getRequestEmployee().getAccount().getId().toString());
			req.setCustomers(event.getIdList());
			return voucherClient.distributeVouchers(req);
		} else {
			BatchDistributionResponse resp = new BatchDistributionResponse();
			// distribute vouchers to all customers
			int currentPage = 0;
			int totalPages = 0;
			int voucherCount = 0;
			int customerCount = 0;
			do {
				Page<Customer> custPage = customerService.findAllByManager(event.getRequestEmployee(), 
						PageRequest.of(currentPage, 100));
				// process current page
				BatchDistributionRequest bdReq = new BatchDistributionRequest();
				bdReq.setCampaignId(event.getCampaignId());
				bdReq.setChannel(event.getChannel());
				bdReq.setMetadata(event.getMetadata());
				bdReq.setReqOrg(event.getRequestEmployee().getOrg().getId().toString());
				bdReq.setReqUser(event.getRequestEmployee().getAccount().getId().toString());
				List<String> ids = new ArrayList<String>();
				custPage.forEach(c->{
					ids.add(c.getAccount().getId().toString());
				});
				bdReq.setCustomers(ids);
				try {
					BatchDistributionResponse response = voucherClient.distributeVouchers(bdReq);
					voucherCount += response.getVoucherCount();
					customerCount += response.getCustomerCount();
				} catch(Exception ex) {
					// errors
				  	resp.setMsg(ex.getMessage());
				}
				currentPage += 1;
				totalPages = custPage.getTotalPages();							
			} while (currentPage <= totalPages 
					&& voucherCount == customerCount 
					&& !StringUtils.hasText(resp.getMsg()));
			
			resp.setCustomerCount(customerCount);
			resp.setVoucherCount(voucherCount);
			
			return resp;
		}
	}

}
