package io.chainmind.myriadapi.service;

import io.chainmind.myriadapi.domain.entity.AppRegistration;

public interface AppRegistrationService {

    AppRegistration findWithOrgByAppId(String appId);
}
