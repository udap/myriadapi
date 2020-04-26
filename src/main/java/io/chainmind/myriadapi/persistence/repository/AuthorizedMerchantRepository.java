package io.chainmind.myriadapi.persistence.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.chainmind.myriadapi.domain.entity.AuthorizedMerchant;

public interface AuthorizedMerchantRepository extends JpaRepository<AuthorizedMerchant, Long>, 
	JpaSpecificationExecutor<AuthorizedMerchant> {
	
	@EntityGraph(attributePaths = { "merchant"})
	AuthorizedMerchant findTopByUpCode(String code);

	@EntityGraph(attributePaths = { "merchant"})
	AuthorizedMerchant findTopByWpCode(String code);

	@EntityGraph(attributePaths = { "merchant"})
	AuthorizedMerchant findTopByApCode(String code);

}
