package io.chainmind.myriadapi.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.chainmind.myriadapi.domain.entity.Account;
import io.chainmind.myriadapi.domain.entity.Employee;
import io.chainmind.myriadapi.domain.entity.Organization;

public interface EmployeeRepository 
	extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

	@Query("SELECT e FROM Employee e join e.org o WHERE o = :org "
			+ "and e.account = :account and e.status = 'ACTIVE' "
			+ "and o.status = 'ACTIVE'")
	Employee findByOrganizationAndAccount(@Param("org")Organization org, @Param("account")Account account);
	
	@Query("SELECT count(e) FROM Employee e JOIN e.org o WHERE o = :org "
			+ "and e.account = :account and e.status = 'ACTIVE' and o.status = 'ACTIVE'")
	int countByOrganizationAndAccount(@Param("org")Organization org, @Param("account")Account account);
}
