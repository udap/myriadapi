package io.chainmind.myriadapi.service;

import java.util.List;

import io.chainmind.myriadapi.domain.CodeName;
import io.chainmind.myriadapi.domain.entity.Organization;

public interface OrganizationService {
	Organization findById(Long id);
	
	Organization findTopAncestor(Organization org);
	
	Organization findByCode(String code, CodeName codeType);
	
	Organization findDescendantByCode(Organization org, String code);
	
	List<Organization> getDescendants(Organization org);
}
