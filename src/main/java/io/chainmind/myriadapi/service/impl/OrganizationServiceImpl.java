package io.chainmind.myriadapi.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import io.chainmind.myriadapi.CacheConfiguration;
import io.chainmind.myriadapi.domain.CodeName;
import io.chainmind.myriadapi.domain.OrganizationStatus;
import io.chainmind.myriadapi.domain.entity.Organization;
import io.chainmind.myriadapi.domain.exception.ApiException;
import io.chainmind.myriadapi.persistence.repository.OrganizationRepository;
import io.chainmind.myriadapi.service.OrganizationService;
import io.chainmind.myriadapi.utils.CommonUtils;

@Service
public class OrganizationServiceImpl implements OrganizationService {

	@Autowired
	private OrganizationRepository orgRepo;
	
	@Cacheable(value = CacheConfiguration.ORGANIZATION_CACHE, unless="#result == null")
	@Override
	public Organization findById(Long id) {
		Optional<Organization> orgOpt = orgRepo.findById(id);
		if (orgOpt.isPresent())
			return orgOpt.get();
		return null;
	}


	@Cacheable(value = CacheConfiguration.ORGANIZATION_ANCESTOR_CACHE, 
			key="#org.id", unless="#result == null")
	@Override
	public Organization findTopAncestor(Organization org) {
		return orgRepo.findTopAncestor(org);
	}
	
    @Cacheable(value = CacheConfiguration.ORGANIZATION_CACHE_BY_CODE, unless = "#result == null")
    @Override
    public Organization findByCode(String code, CodeName codeType) {
        Organization organization = null;
        if (codeType.equals(CodeName.ID)) {
        	Optional<Organization> organizationOpt = orgRepo.findById(Long.valueOf(code));
        	if (organizationOpt.isPresent())
        		organization = organizationOpt.get();
        } else if (codeType.equals(CodeName.CODE)) {
        	// parse code 
        	String[] parts = CommonUtils.parseCode(code);
        	if (parts.length != 2)
        		throw new ApiException(HttpStatus.BAD_REQUEST,"code.invalid");
        	Optional<Organization> orgOpt = orgRepo.findById(Long.valueOf(parts[0]));
        	if (!orgOpt.isPresent())
        		throw new ApiException(HttpStatus.NOT_FOUND,"organization.rootNotFound");
        	organization = orgRepo.findDescendantByCode(orgOpt.get(), parts[1]);
    	} else if (codeType.equals(CodeName.LICENSE)) {
        	organization = orgRepo.findByLicenseNo(code);
        } else if (codeType.equals(CodeName.UPCODE)) {
        	organization = orgRepo.findByUpCode(code);
        } else if (codeType.equals(CodeName.APCODE)) {
        	organization = orgRepo.findByApCode(code);
        } else if (codeType.equals(CodeName.WPCODE)) {
        	organization = orgRepo.findByWpCode(code);
        } else {
            throw new ApiException(HttpStatus.BAD_REQUEST, "organization.unknownCodeType");
        }
        
        if (organization == null)
            throw new ApiException(HttpStatus.NOT_FOUND, "organization.notFound");
        
        if (!organization.getStatus().equals(OrganizationStatus.ACTIVE))
        	throw new ApiException(HttpStatus.FORBIDDEN, "organization.inactive");
        
        return organization;
    }

    @Override
    public Organization findDescendantByCode(Organization org, String code) {
        return orgRepo.findDescendantByCode(org, code);
    }
    
//	@Override
//	public Page<Organization> findSubsidiaries(Organization org,String searchTxt,Pageable pageable) {
//		if (StringUtils.hasText(searchTxt)){
//			return orgRepo.findAllByParentAndSearchTxt(org,"%"+searchTxt+"%",pageable);
//		}
//		return orgRepo.findAllByParent(org,pageable);	
//	}
}
