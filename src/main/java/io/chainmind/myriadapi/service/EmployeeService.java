package io.chainmind.myriadapi.service;

import io.chainmind.myriadapi.domain.entity.Account;
import io.chainmind.myriadapi.domain.entity.Employee;
import io.chainmind.myriadapi.domain.entity.Organization;

public interface EmployeeService {
	Employee findByOrganizationAndAccount(Organization org, Account account);
	int countByOrganizationAndAccount(Organization org, Account account);
}
