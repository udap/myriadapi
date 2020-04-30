package io.chainmind.myriadapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import io.chainmind.myriadapi.CacheConfiguration;
import io.chainmind.myriadapi.domain.entity.Account;
import io.chainmind.myriadapi.domain.entity.Employee;
import io.chainmind.myriadapi.domain.entity.Organization;
import io.chainmind.myriadapi.persistence.repository.EmployeeRepository;
import io.chainmind.myriadapi.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	
	@Autowired
	private EmployeeRepository employeeRepo;

	@Cacheable(value = CacheConfiguration.EMPLOYEE_CACHE, unless="#result == null")
	@Override
	public Employee findByOrganizationAndAccount(Organization org, Account account) {
		return employeeRepo.findByOrganizationAndAccount(org, account);
	}

	@Override
	public int countByOrganizationAndAccount(Organization org, Account account) {
		return employeeRepo.countByOrganizationAndAccount(org, account);
	}

}
