package io.chainmind.myriadapi.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.chainmind.myriad.domain.common.Audience;
import io.chainmind.myriad.domain.common.Channel;
import io.chainmind.myriad.domain.dto.campaign.CampaignResponse;
import io.chainmind.myriad.domain.dto.distribution.BatchDistributionResponse;
import io.chainmind.myriad.domain.dto.distribution.CollectVoucherRequest;
import io.chainmind.myriad.domain.dto.distribution.DistributeVoucherRequest;
import io.chainmind.myriad.domain.dto.distribution.DistributeVoucherResponse;
import io.chainmind.myriadapi.client.VoucherClient;
import io.chainmind.myriadapi.domain.CodeName;
import io.chainmind.myriadapi.domain.RequestUser;
import io.chainmind.myriadapi.domain.dto.ApiCollectVoucherRequest;
import io.chainmind.myriadapi.domain.dto.BatchStatus;
import io.chainmind.myriadapi.domain.dto.Code;
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
import io.chainmind.myriadapi.utils.CommonUtils;

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
		
		// TODO: ensure organization is part of the app domain
		
		Account mgrAccount = accountService.findByCode(req.getReqUser(), CodeName.ID);
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

		String serviceOrgId = customer.getOrg().getId().toString();
		if (StringUtils.hasText(req.getCustomerOrgId()))
			serviceOrgId = req.getCustomerOrgId();
		//组装请求数据
		Audience audience = Audience.builder()
				.id(customer.getAccount().getId().toString())
				.serviceOrg(Audience.ServiceOrg.builder()
						.org(serviceOrgId)
						.build())
				.build();
		DistributeVoucherRequest voucherRequest = DistributeVoucherRequest.builder()
				.reqUser(mgrAccount.getId().toString())
				.reqOrg(org.getId().toString())
				.campaignId(req.getCampaignId())
				.voucherId(req.getVoucherId())
				.channel(req.getChannel().name())
				.metadata(req.getMetadata())
				.audience(audience)
				.build();
		return voucherClient.distributeVoucher(voucherRequest);
	}

	/**
	 * Distribute voucher to an account for a campaign
	 * @param req 
	 * @return
	 */
	@PostMapping("/collect")
	public DistributeVoucherResponse create(@Valid @RequestBody ApiCollectVoucherRequest req){
		Code aCode = CommonUtils.uniqueCode(requestUser.getAppOrg().getId().toString(), req.getAccountCode());
		Account account = accountService.findByCode(aCode.getValue(), aCode.getName());
		if (Objects.isNull(account))
			account = accountService.register(aCode.getValue(), aCode.getName());
		// ensure campaign is within the scope of the registered organization
		CampaignResponse campaign = voucherClient.getCampaign(req.getCampaignId());
		Organization org = orgService.findById(Long.valueOf(campaign.getOwner()));
		Organization topAncestor = orgService.findTopAncestor(org);
		if (!Objects.equals(requestUser.getAppOrg().getId(), topAncestor.getId()))
			throw new ApiException(HttpStatus.UNAUTHORIZED, "organization.unauthorized");
		
		//组装请求数据
		Audience audience = Audience.builder()
				.id(account.getId().toString())
				.build();
		CollectVoucherRequest request = CollectVoucherRequest.builder()
				.campaignId(req.getCampaignId())
				.audience(audience)
				.channel(Channel.API.name())
				.metadata(req.getMetadata())
				.build();
		DistributeVoucherResponse response = voucherClient.create(request);
//		customerService.findOrCreate(org, currentAccount, null, "个人版直接领券加入");
		return response;

	}
	
	@PostMapping("/batch")
	public BatchDistributionResponse distributeVouchers(@Valid @RequestBody DistributeToCustomersRequest req) {
		// prepare BatchDistributionResponse
		BatchDistributionResponse resp = new BatchDistributionResponse();

		Map<Long, Audience> audienceMap = new HashMap<Long,Audience>();
		try {
			// ensure a valid code type for customer
			if (CodeName.CELLPHONE.equals(req.getAccountCodeType()) ||
					CodeName.EMAIL.equals(req.getAccountCodeType()) ||
					CodeName.ID.equals(req.getAccountCodeType())) {
			
				// retrieve organization 
				Organization org = orgService.findById(Long.valueOf(req.getReqOrg()));
				if (!org.isActive())
					throw new ApiException(HttpStatus.NOT_FOUND, "organization.notFound");
				
				Account mgrAccount = accountService.findByCode(req.getReqUser(), CodeName.ID);
				if (mgrAccount == null || !mgrAccount.isEnabled())
					throw new ApiException(HttpStatus.NOT_FOUND, "account.notFound");
				
				// query customerManager based on reqUser and reqOrg
				Employee mgrEmployee = employeeService.findByOrganizationAndAccount(org, mgrAccount);
				if (mgrEmployee == null || !mgrEmployee.isActive())
					throw new ApiException(HttpStatus.NOT_FOUND, "employee.notFound");
					
				int customerCount = 0;
				List<Audience> audiences = new ArrayList<>();
				if (req.isAll()) {
					// query total number of active customers
					customerCount = employeeService.countByOrganizationAndAccount(org, mgrAccount);
				} else {
					// validate codes in the request
					for (String code: req.getAccounts()) {
						Account account = accountService.findByCode(code, req.getAccountCodeType());
						Customer customer = null;
						if (account != null) {
							if (req.getStrictMode().equals(DistributionMode.REQUSER_ONLY)) {
								// is the account a customer of the reqUser?
								customer = customerService.findByAccountAndManager(account, mgrEmployee);
								
							} else if (req.getStrictMode().equals(DistributionMode.REQORG_ONLY)) {
								// is the account a customer of the reqOrg?
								customer = customerService.findByAccountAndOrganization(account, org);								
							}
							if (customer == null)
								throw new ApiException(HttpStatus.NOT_FOUND, "customer.notFound");
							Audience audience = Audience.builder()
									.id(account.getId().toString())
									.serviceOrg(Audience.ServiceOrg.builder()
											.org(customer.getOrg().getId().toString())
											.build())
									.build();
//							audience.addFollowing(Following.builder()
//									.org(customer.getOrg().getId().toString())
//									.tags(customer.getTags())
//									.build());
							audienceMap.putIfAbsent(account.getId(), audience);
						}
					}
					// replace with clean data
					audiences.addAll(audienceMap.values());
					customerCount = audiences.size();
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
							.audiences(audiences)
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
