package io.chainmind.myriadapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.chainmind.myriadapi.domain.entity.AuthorizedMerchant;
import io.chainmind.myriadapi.domain.entity.Organization;
import io.chainmind.myriadapi.persistence.repository.AuthorizedMerchantRepository;
import io.chainmind.myriadapi.service.AuthorizedMerchantService;

@Service
public class AuthorizedMerchantServiceImpl implements AuthorizedMerchantService {
    @Autowired
    private AuthorizedMerchantRepository merchantRepo;

	@Override
	public AuthorizedMerchant find(Organization marketer, Organization merchant) {
		return merchantRepo.findByOrgAndMerchant(marketer, merchant);
	}

	@Override
	public boolean existsInDescendant(Organization marketer, Organization merchant) {
		return merchantRepo.countInDescendants(marketer, merchant) > 0;
	}

}
