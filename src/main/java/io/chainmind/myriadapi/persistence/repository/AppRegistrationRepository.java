package io.chainmind.myriadapi.persistence.repository;

import io.chainmind.myriadapi.domain.entity.AppRegistration;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AppRegistrationRepository extends JpaRepository<AppRegistration, Long>, JpaSpecificationExecutor<AppRegistration> {

	@EntityGraph(attributePaths = { "org"})
	@Query("SELECT a FROM AppRegistration a WHERE a.appId = :appId")
	AppRegistration findWithOrgByAppId(@Param("appId") String appId);


}
