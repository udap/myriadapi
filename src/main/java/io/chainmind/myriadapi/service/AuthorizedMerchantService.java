package io.chainmind.myriadapi.service;

import io.chainmind.myriadapi.domain.entity.AuthorizedMerchant;
import io.chainmind.myriadapi.domain.entity.Organization;

public interface AuthorizedMerchantService {
	AuthorizedMerchant find(Organization marketer, Organization merchant);
}
