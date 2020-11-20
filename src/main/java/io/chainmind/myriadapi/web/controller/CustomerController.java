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
		return findByCode("ID:"+id);
	}
	
	/**
	 * Query a customer by its code
	 * @param code a string in the form of name:value pair. name is one of the <code>CodeName</code>
	 * @return
	 */
	@GetMapping
	public CustomerResponse findByCode(@RequestParam(name="code")String code) {
		String[] parts = CommonUtils.parseCode(code);
		if (parts.length != 2)
			throw new ApiException(HttpStatus.BAD_REQUEST,"code.illegal");
		Code aCode = Code.builder()
				.name(CodeName.valueOf(parts[0]))
				.value(parts[1])
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
