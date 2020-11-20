package io.chainmind.myriadapi.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.chainmind.myriadapi.domain.entity.Organization;

public interface OrganizationRepository
		extends JpaRepository<Organization, Long>, JpaSpecificationExecutor<Organization> {
	
	@Query("SELECT d FROM OrganizationClosure c join c.descendant d " +
			"WHERE c.ancestor = :org and d.code = :code and d.status != 'DELETED'  ")
	Organization findDescendantByCode(@Param("org") Organization org,@Param("code")String code);

	@Query("SELECT org  FROM Organization org WHERE org.licenseNo = :licenseNo AND org.status != 'DELETED' ")
	Organization findByLicenseNo(@Param("licenseNo")String licenseNo);

	@Query("SELECT org  FROM Organization org WHERE org.upCode = :upCode AND org.status != 'DELETED'")
	Organization findByUpCode(@Param("upCode")String upCode);

	@Query("SELECT org  FROM Organization org WHERE org.wpCode = :wpCode AND org.status != 'DELETED'")
	Organization findByWpCode(@Param("wpCode")String wpCode);

	@Query("SELECT org  FROM Organization org WHERE org.apCode = :apCode AND org.status != 'DELETED' ")
	Organization findByApCode(@Param("apCode")String apCode);

	@Query("SELECT o from Organization o JOIN OrganizationClosure c ON c.ancestor = o "
			+ "WHERE c.descendant = :org and c.pathLength>=0 and o.parent is null and o.status != 'DELETED'")
	Organization findTopAncestor(@Param("org")Organization org);

}
