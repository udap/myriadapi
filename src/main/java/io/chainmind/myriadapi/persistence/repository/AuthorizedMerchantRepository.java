package io.chainmind.myriadapi.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.chainmind.myriadapi.domain.entity.AuthorizedMerchant;
import io.chainmind.myriadapi.domain.entity.Organization;

public interface AuthorizedMerchantRepository extends JpaRepository<AuthorizedMerchant, Long>, 
	JpaSpecificationExecutor<AuthorizedMerchant> {
	
	AuthorizedMerchant findByOrgAndMerchant(Organization org, Organization merchant);
	
	@Query("SELECT count(m) FROM AuthorizedMerchant am join am.merchant m JOIN am.org b "
			+ "JOIN OrganizationClosure oc ON b = oc.descendant "
			+ "WHERE oc.ancestor = :marketer and oc.pathLength > 0 and b.status = 'ACTIVE' "
			+ "and m = :merchant and m.status = 'ACTIVE'")
	int countInDescendants(@Param("marketer")Organization marketer, @Param("merchant")Organization merchant);

}
