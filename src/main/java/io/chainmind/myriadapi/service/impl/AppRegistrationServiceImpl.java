package io.chainmind.myriadapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import io.chainmind.myriadapi.CacheConfiguration;
import io.chainmind.myriadapi.domain.entity.AppRegistration;
import io.chainmind.myriadapi.persistence.repository.AppRegistrationRepository;
import io.chainmind.myriadapi.service.AppRegistrationService;

@Service
public class AppRegistrationServiceImpl implements AppRegistrationService {

    @Autowired
    private AppRegistrationRepository appRegistrationRepo;


	@Cacheable(value = CacheConfiguration.REGISTRY_CACHE, unless="#result == null")
	@Override
    public AppRegistration findWithOrgByAppId(String appId) {
        return appRegistrationRepo.findWithOrgByAppId(appId);
    }
}
