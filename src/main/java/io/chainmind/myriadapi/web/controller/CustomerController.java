package io.chainmind.myriadapi.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	
	private CustomerResponse queryByCode(Code code) {
		Code aCode = CommonUtils.uniqueCode(requestUser.getAppOrg().getId().toString(), code);
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
	
	@GetMapping("/{id}")
	public CustomerResponse findById(@PathVariable String id) {
		Account account = accountService.findById(id);
		if (Objects.isNull(account))
			throw new ApiException(HttpStatus.NOT_FOUND,"account.notFound");
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
	
	/**
	 * Query customers by codes
	 * @param code a comma separated name:value string. name is one of ID,CELLPHONE,EMAIL,SOURCE_ID.
	 * @return
	 */
	@GetMapping("/search")
	public List<CustomerResponse> findAllByCodes(@RequestParam(name="codes")String codes) {
		List<Code> parsedCodes = CommonUtils.parseMixedCode(codes);
		if (parsedCodes.isEmpty())
			throw new ApiException(HttpStatus.BAD_REQUEST,"code.illegal");
		List<CustomerResponse> results = new ArrayList<>();
		parsedCodes.forEach(aCode->results.add(queryByCode(aCode)));
		return results;
	}
	
	@GetMapping("/search/{name}/{value}")
	public CustomerResponse findByCode(@PathVariable String name, @PathVariable String value) {
		List<Code> parsedCodes = CommonUtils.parseMixedCode(name+":"+value);
		if (parsedCodes.size() != 1)
			throw new ApiException(HttpStatus.BAD_REQUEST,"code.illegal");
		return queryByCode(parsedCodes.get(0));
	}
}
