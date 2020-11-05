package io.chainmind.myriadapi.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.chainmind.myriadapi.domain.entity.Account;

public interface AccountRepository 
	extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {
	
	Account findByCellphone(String cellphone);
	
	Account findByName(String name);
	
	Account findByEmail(String email);
	
	Account findByOrganizationIdAndSourceId(String organizationId, String sourceId);
}
