package io.chainmind.myriadapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.chainmind.myriadapi.domain.entity.Account;
import io.chainmind.myriadapi.domain.entity.Customer;
import io.chainmind.myriadapi.domain.entity.Employee;
import io.chainmind.myriadapi.domain.entity.Organization;

public interface CustomerService {
	Customer findByAccountAndOrganization(Account account, Organization org);
	
	Customer findByAccountAndManager(Account account, Employee manager);
	
	Page<Customer> findAllByManager(Employee manager, Pageable pageable);
}
