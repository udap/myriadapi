package io.chainmind.myriadapi.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.chainmind.myriadapi.domain.entity.AuthorizedMerchant;

public interface AuthorizedMerchantRepository extends JpaRepository<AuthorizedMerchant, Long>, 
	JpaSpecificationExecutor<AuthorizedMerchant> {
	
	AuthorizedMerchant findTopByUpCode(String code);

	AuthorizedMerchant findTopByWpCode(String code);

	AuthorizedMerchant findTopByApCode(String code);

}
