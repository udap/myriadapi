package io.chainmind.myriadapi.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.chainmind.myriadapi.domain.entity.Organization;

public interface OrganizationRepository
		extends JpaRepository<Organization, Long>, JpaSpecificationExecutor<Organization> {

	/*
	 * Query all direct and indirect subsidiaries
	 */
	@Query(value = "SELECT o.id FROM organization o JOIN org_closure c " +
			" ON (o.id = c.descendant_id) " +
			" WHERE c.ancestor_id = :id and c.path_length>0 and o.status != 'DELETED'", nativeQuery = true)
    List<Long> findAllSubsidiaries(@Param("id") Long orgId);

}
