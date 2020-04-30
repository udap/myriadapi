package io.chainmind.myriadapi.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import io.chainmind.myriadapi.domain.entity.Account;
import io.chainmind.myriadapi.domain.entity.Customer;
import io.chainmind.myriadapi.domain.entity.Employee;
import io.chainmind.myriadapi.domain.entity.Organization;

public interface CustomerRepository 
	extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer>{

	@Query("SELECT c FROM Customer c JOIN c.org o WHERE c.account = :account "
			+ "and c.org = org and o.status = 'ACTIVE' limit 1")
	Customer findTopByAccountAndOrganization(Account account, Organization org);
	
	Customer findByAccountAndManager(Account account, Employee manager);
	
	Page<Customer> findAllByManager(Employee employee, Pageable pageable);
}
