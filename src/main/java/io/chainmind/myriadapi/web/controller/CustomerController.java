package io.chainmind.myriadapi.web.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.chainmind.myriadapi.domain.CodeName;
import io.chainmind.myriadapi.domain.RequestUser;
import io.chainmind.myriadapi.domain.dto.Code;
import io.chainmind.myriadapi.domain.dto.CustomerResponse;
import io.chainmind.myriadapi.domain.entity.Account;
import io.chainmind.myriadapi.domain.entity.Customer;
import io.chainmind.myriadapi.domain.exception.ApiException;
import io.chainmind.myriadapi.service.AccountService;
import io.chainmind.myriadapi.service.CustomerService;
import io.chainmind.myriadapi.utils.CommonUtils;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

	@Autowired
	private AccountService accountService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private RequestUser requestUser;
	
	@GetMapping("/{id}")
	public CustomerResponse findById(@PathVariable String id) {
		Account account = accountService.findById(id);
		if (Objects.isNull(account))
			throw new ApiException(HttpStatus.NOT_FOUND,"account.notFound");
		Customer customer = customerService.findByAccountAndOrganization(account, requestUser.getAppOrg());
		if (Objects.isNull(customer))
			throw new ApiException(HttpStatus.NOT_FOUND,"customer.notFound");
		return CustomerResponse.builder()
				.id(id)
				.name(account.getName())
				.realName(customer.getName())
				.cellphone(account.getCellphone())
				.email(account.getEmail())
				.sourceId(account.getSourceId())
				.tags(customer.getTags())
				.build();	
	}
	
	@GetMapping
	public CustomerResponse findByCode(@RequestParam(name="codeValue")String codeValue,
			@RequestParam(name="codeName",required=false,defaultValue="ID")CodeName codeName) {
		Code aCode = Code.builder()
				.name(codeName)
				.value(codeValue)
				.build();
		aCode = CommonUtils.uniqueCode(requestUser.getAppOrg().getId().toString(), aCode);
		Account account = accountService.findByCode(aCode.getValue(), aCode.getName());
		Customer customer = customerService.findByAccountAndOrganization(account, requestUser.getAppOrg());
		if (Objects.isNull(customer))
			throw new ApiException(HttpStatus.NOT_FOUND,"customer.notFound");
		return CustomerResponse.builder()
				.id(account.getId().toString())
				.name(account.getName())
				.realName(customer.getName())
				.cellphone(account.getCellphone())
				.email(account.getEmail())
				.sourceId(account.getSourceId())
				.tags(customer.getTags())
				.build();	
	}
}
