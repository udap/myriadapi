package io.chainmind.myriadapi.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import io.chainmind.myriadapi.domain.RequestUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.chainmind.myriad.domain.dto.distribution.BatchDistributionResponse;
import io.chainmind.myriad.domain.dto.distribution.DistributeVoucherRequest;
import io.chainmind.myriad.domain.dto.distribution.DistributeVoucherResponse;
import io.chainmind.myriadapi.client.VoucherClient;
import io.chainmind.myriadapi.domain.CodeType;
import io.chainmind.myriadapi.domain.dto.BatchStatus;
import io.chainmind.myriadapi.domain.dto.DistributeToCustomersRequest;
import io.chainmind.myriadapi.domain.dto.DistributeToSingleCustomerRequest;
import io.chainmind.myriadapi.domain.dto.DistributionMode;
import io.chainmind.myriadapi.domain.entity.Account;
import io.chainmind.myriadapi.domain.entity.Customer;
import io.chainmind.myriadapi.domain.entity.Employee;
import io.chainmind.myriadapi.domain.entity.Organization;
import io.chainmind.myriadapi.domain.exception.ApiException;
import io.chainmind.myriadapi.event.BatchDistributionEvent;
import io.chainmind.myriadapi.event.EventPublisher;
import io.chainmind.myriadapi.service.AccountService;
import io.chainmind.myriadapi.service.CustomerService;
import io.chainmind.myriadapi.service.EmployeeService;
import io.chainmind.myriadapi.service.OrganizationService;

@RestController
@RequestMapping("/api/distributions")
public class DistributionController {
	@Autowired
	private VoucherClient voucherClient;

	@Autowired
	private RequestUser requestUser;
	@Autowired
	private AccountService accountService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private OrganizationService orgService;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private EventPublisher eventPublisher;
	
	@PostMapping
	public DistributeVoucherResponse distributeVoucher(@Valid @RequestBody DistributeToSingleCustomerRequest req) {
		if (!StringUtils.hasText(req.getCampaignId()) && !StringUtils.hasText(req.getVoucherId()))
			throw new ApiException(HttpStatus.BAD_REQUEST, "distribution.missingParams");
		
		// retrieve organization 
		Organization org = orgService.findById(Long.valueOf(req.getReqOrg()));
		if (!org.isActive())
			throw new ApiException(HttpStatus.NOT_FOUND, "organization.notFound");
		
		Account mgrAccount = accountService.findByCode(req.getReqUser(), CodeType.ID);
		if (mgrAccount == null || !mgrAccount.isEnabled())
			throw new ApiException(HttpStatus.NOT_FOUND, "account.notFound");
		
		// query customerManager based on reqUser and reqOrg
		Employee mgrEmployee = employeeService.findByOrganizationAndAccount(org, mgrAccount);
		if (mgrEmployee == null || !mgrEmployee.isActive())
			throw new ApiException(HttpStatus.NOT_FOUND, "employee.notFound");

		Account customerAccount = accountService.findByCode(req.getCustomerId(), req.getCustomerIdType());
		Customer customer = customerService.findByAccountAndManager(customerAccount, mgrEmployee);
		if (customer == null) 
			throw new ApiException(HttpStatus.NOT_FOUND, "customer.notFound");

		DistributeVoucherRequest mReq = new DistributeVoucherRequest();
		mReq.setChannel(req.getChannel());
		mReq.setCustomerId(customer.getId().toString());
		mReq.setMetadata(req.getMetadata());
		mReq.setReqOrg(req.getReqOrg());
		mReq.setReqUser(req.getReqUser());
		mReq.setVoucherId(req.getVoucherId());
		mReq.setCampaignId(req.getCampaignId());
		requestUser.setReqUser(mReq.getReqUser());
		return voucherClient.distributeVoucher(mReq);
	}
	
	@PostMapping("/batch")
	public BatchDistributionResponse distributeVouchers(@Valid @RequestBody DistributeToCustomersRequest req) {
		// prepare BatchDistributionResponse
		BatchDistributionResponse resp = new BatchDistributionResponse();

		Map<Long, String> idMap = new HashMap<Long,String>();
		try {
			// ensure a valid code type for customer
			if (CodeType.CELLPHONE.equals(req.getAccountCodeType()) ||
					CodeType.EMAIL.equals(req.getAccountCodeType()) ||
					CodeType.ID.equals(req.getAccountCodeType())) {
			
				// retrieve organization 
				Organization org = orgService.findById(Long.valueOf(req.getReqOrg()));
				if (!org.isActive())
					throw new ApiException(HttpStatus.NOT_FOUND, "organization.notFound");
				
				Account mgrAccount = accountService.findByCode(req.getReqUser(), CodeType.ID);
				if (mgrAccount == null || !mgrAccount.isEnabled())
					throw new ApiException(HttpStatus.NOT_FOUND, "account.notFound");
				
				// query customerManager based on reqUser and reqOrg
				Employee mgrEmployee = employeeService.findByOrganizationAndAccount(org, mgrAccount);
				if (mgrEmployee == null || !mgrEmployee.isActive())
					throw new ApiException(HttpStatus.NOT_FOUND, "employee.notFound");
					
				int customerCount = 0;
				List<String> ids = new ArrayList<String>();
				if (req.isAll()) {
					// query total number of active customers
					customerCount = employeeService.countByOrganizationAndAccount(org, mgrAccount);
				} else {
					// validate codes in the request
					for (String code: req.getAccounts()) {
						Account account = accountService.findByCode(code, req.getAccountCodeType());
						if (account != null) {
							if (req.getStrictMode().equals(DistributionMode.REQUSER_ONLY)) {
								// is the account a customer of the reqUser?
								Customer customer = customerService.findByAccountAndManager(account, mgrEmployee);
								if (customer == null) 
									throw new ApiException(HttpStatus.NOT_FOUND, "customer.notFound");
									
								
							} else if (req.getStrictMode().equals(DistributionMode.REQORG_ONLY)) {
								// is the account a customer of the reqOrg?
								Customer customer = customerService.findByAccountAndOrganization(account, org);
								if (customer == null)
									throw new ApiException(HttpStatus.NOT_FOUND, "customer.notFound");
								
							}
							idMap.putIfAbsent(account.getId(), (account.getId().toString()));
						}
					}
					// replace with clean data
					ids.addAll(idMap.values());
					customerCount = ids.size();
				}
				// 
				resp.setCustomerCount(customerCount);
				if (customerCount > 0) {
					resp.setStatus(BatchStatus.PENDING);
					resp.setMsg("distribution.batchExecuting");
					// create batch distribution event
					BatchDistributionEvent event = BatchDistributionEvent.builder()
							.all(req.isAll())
							.campaignId(req.getCampaignId())
							.channel(req.getChannel())
							.metadata(req.getMetadata())
							.requestEmployee(mgrEmployee)
							.idList(ids)
							.build();			
					// create batch distribution event and publish
					eventPublisher.activateBatchDistribution(event);
				} else {
					resp.setStatus(BatchStatus.SUCCESS);
				}

			} 
		} catch(Exception ex) {
			resp.setStatus(BatchStatus.FAILED);
			resp.setMsg(ex.getMessage());
		}
		
		return resp;
	}
}
