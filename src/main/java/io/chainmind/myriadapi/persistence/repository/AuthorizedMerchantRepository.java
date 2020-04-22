package io.chainmind.myriadapi.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import io.chainmind.myriadapi.domain.entity.AuthorizedMerchant;
import io.chainmind.myriadapi.domain.entity.Organization;

public interface AuthorizedMerchantRepository extends JpaRepository<AuthorizedMerchant, Long>, 
	JpaSpecificationExecutor<AuthorizedMerchant> {

	@Query("SELECT m FROM AuthorizedMerchant am JOIN am.merchant m WHERE am.org=:authoringOrg and m.license=:code")
	Organization findByLicense(String code, Organization authoringOrg);
	
	@Query("SELECT m FROM AuthorizedMerchant am JOIN am.merchant m WHERE am.org=:authoringOrg and am.upCode=:code")
	Organization findByUpCode(String code, Organization authoringOrg);

	@Query("SELECT m FROM AuthorizedMerchant am JOIN am.merchant m WHERE am.org=:authoringOrg and am.wpCode=:code")
	Organization findByWpCode(String code, Organization authoringOrg);

	@Query("SELECT m FROM AuthorizedMerchant am JOIN am.merchant m WHERE am.org=:authoringOrg and am.apCode=:code")
	Organization findByApCode(String code, Organization authoringOrg);

}
