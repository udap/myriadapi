package io.chainmind.myriadapi.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import io.chainmind.myriadapi.CacheConfiguration;
import io.chainmind.myriadapi.domain.CodeType;
import io.chainmind.myriadapi.domain.OrganizationStatus;
import io.chainmind.myriadapi.domain.entity.Organization;
import io.chainmind.myriadapi.domain.exception.ApiException;
import io.chainmind.myriadapi.persistence.repository.OrganizationRepository;
import io.chainmind.myriadapi.service.OrganizationService;

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
    public Organization findByCode(String code, CodeType codeType) {
        Organization merchant = null;
        if (codeType.equals(CodeType.ID)) {
        	Optional<Organization> merchantOpt = orgRepo.findById(Long.valueOf(code));
        	if (merchantOpt.isPresent())
        		merchant = merchantOpt.get();
        }
        else if (codeType.equals(CodeType.LICENSE))
            merchant = orgRepo.findTopByLicenseNo(code);
        else if (codeType.equals(CodeType.UPCODE))
            merchant = orgRepo.findTopByUpCode(code);
        else if (codeType.equals(CodeType.APCODE))
            merchant = orgRepo.findTopByApCode(code);
        else if (codeType.equals(CodeType.WPCODE))
            merchant = orgRepo.findTopByWpCode(code);
        else
            throw new ApiException(HttpStatus.BAD_REQUEST, "unknown code type");

        if (merchant == null)
            throw new ApiException(HttpStatus.NOT_FOUND, "merchant.notFound");
        if (!merchant.getStatus().equals(OrganizationStatus.ACTIVE))
        	throw new ApiException(HttpStatus.FORBIDDEN, "merchant.inactive");
        
        return merchant;
    }


}
