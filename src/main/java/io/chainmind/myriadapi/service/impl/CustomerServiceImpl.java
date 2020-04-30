package io.chainmind.myriadapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.chainmind.myriadapi.CacheConfiguration;
import io.chainmind.myriadapi.domain.entity.Account;
import io.chainmind.myriadapi.domain.entity.Customer;
import io.chainmind.myriadapi.domain.entity.Employee;
import io.chainmind.myriadapi.domain.entity.Organization;
import io.chainmind.myriadapi.persistence.repository.CustomerRepository;
import io.chainmind.myriadapi.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	private CustomerRepository customerRepo;

	@Cacheable(value = CacheConfiguration.CUSTOMER_BY_ACCT_ORG_CACHE, unless="#result == null")
	@Override
	public Customer findByAccountAndOrganization(Account account, Organization org) {
		return customerRepo.findTopByAccountAndOrganization(account, org);
	}

	@Override
	public Customer findByAccountAndManager(Account account, Employee manager) {
		return customerRepo.findByAccountAndManager(account, manager);
	}

	@Override
	public Page<Customer> findAllByManager(Employee manager, Pageable pageable) {
		return customerRepo.findAllByManager(manager, pageable);
	}

}
