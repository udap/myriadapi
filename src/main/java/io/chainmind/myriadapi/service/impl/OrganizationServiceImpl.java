package io.chainmind.myriadapi.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.chainmind.myriadapi.domain.entity.Organization;
import io.chainmind.myriadapi.persistence.repository.OrganizationRepository;
import io.chainmind.myriadapi.service.OrganizationService;

@Service
public class OrganizationServiceImpl implements OrganizationService {

	@Autowired
	private OrganizationRepository orgRepo;
	
	@Override
	public Organization findById(Long id) {
		Optional<Organization> orgOpt = orgRepo.findById(id);
		if (orgOpt.isPresent())
			return orgOpt.get();
		return null;
	}

}
