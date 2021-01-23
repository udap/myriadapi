package io.chainmind.myriadapi.service;

import java.util.Objects;
import java.util.Optional;

import io.chainmind.myriad.domain.common.Merchant;
import io.chainmind.myriadapi.domain.entity.AuthorizedMerchant;
import io.chainmind.myriadapi.domain.entity.Organization;

public class ValidationUtils {
	/**
	 * Prepare Merchant facts for validation
	 * @param merchant the merchant organization
	 * @param ancestor top ancestor of the merchant
	 * @param am is the merchant an authorized merchant
	 * @param amAncestor is the merchant ancestor an authorized merchant?
	 * @return
	 */
	public static Optional<Merchant> prepareMerchantFacts(Organization merchant, Organization mAncestor,
			AuthorizedMerchant am, AuthorizedMerchant amAncestor, 
			boolean merchantIsAuthorized, boolean ancestorIsAuthorized) {
		// ensure merchant and its ancestor cannot be null
		assert(merchant != null && mAncestor != null);
		// neither merchant nor its top ancestor is an authorized merchant  
		if (Objects.isNull(am) && Objects.isNull(amAncestor) && !merchantIsAuthorized && !ancestorIsAuthorized)
			return Optional.empty();

		Merchant ma = null;
		// merchant ancestor is an authorized merchant (and is not merchant itself)
		if ((ancestorIsAuthorized || Objects.nonNull(amAncestor)) 
				&& !Objects.equals(merchant.getId(), mAncestor.getId()))
			ma = Merchant.builder()
				.id(mAncestor.getId().toString())
				.build();
		// set merchant tags if merchant is a direct authorized merchant
		// or set tags if merchant ancestor is a direct authorized merchant
		String tags = null;
		if (Objects.nonNull(am))
			tags = am.getTags();
		else if (Objects.nonNull(amAncestor))
			tags = amAncestor.getTags();
		
		// build merchant object
		return Optional.of(Merchant.builder()
				.id(merchant.getId().toString())
				.province(merchant.getProvince())
				.city(merchant.getCity())
				.district(merchant.getDistrict())
				.tags(tags)
				.topAncestor(ma)
				.build());
	
	}

}
