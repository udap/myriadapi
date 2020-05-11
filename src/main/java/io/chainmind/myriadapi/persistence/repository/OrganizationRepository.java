package io.chainmind.myriadapi.persistence.repository;

import io.chainmind.myriadapi.domain.entity.AuthorizedMerchant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.chainmind.myriadapi.domain.entity.Organization;

public interface OrganizationRepository
		extends JpaRepository<Organization, Long>, JpaSpecificationExecutor<Organization> {

	Organization findTopByLicenseNo(String code);

	Organization findTopByUpCode(String code);

	Organization findTopByWpCode(String code);

	Organization findTopByApCode(String code);

	@Query("SELECT o from Organization o JOIN OrganizationClosure c ON c.ancestor = o "
			+ "WHERE c.descendant = :org and c.pathLength>=0 and o.parent is null and o.status != 'DELETED'")
	Organization findTopAncestor(@Param("org")Organization org);

}
