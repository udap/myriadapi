package io.chainmind.myriadapi.service;

import io.chainmind.myriadapi.domain.entity.Organization;

public interface OrganizationService {
	Organization findById(Long id);
	
	Organization findTopAncestor(Organization org);
}
