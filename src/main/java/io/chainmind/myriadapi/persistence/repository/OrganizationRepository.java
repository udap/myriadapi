package io.chainmind.myriadapi.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.chainmind.myriadapi.domain.entity.Organization;

public interface OrganizationRepository
		extends JpaRepository<Organization, Long>, JpaSpecificationExecutor<Organization> {

}
