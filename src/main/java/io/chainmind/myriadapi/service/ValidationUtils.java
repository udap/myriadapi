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
	 * @param am authorized merchant
	 * @param amAncestor authorized ancestor
	 * @return
	 */
	public static Optional<Merchant> prepareMerchantFacts(Organization merchant, Organization ancestor,
			AuthorizedMerchant am, AuthorizedMerchant amAncestor) {
		// ensure merchant and its ancestor cannot be null
		assert(merchant != null && ancestor != null);
		// neither merchant nor its top ancestor is authorized 
		if (Objects.isNull(am) && Objects.isNull(amAncestor))
			return Optional.empty();

		Merchant mAncestor = null;
		if (Objects.nonNull(amAncestor) && !Objects.equals(merchant.getId(), ancestor.getId()))
			mAncestor = Merchant.builder()
				.id(ancestor.getId().toString())
				.build();
		
		return Optional.of(Merchant.builder()
				.id(merchant.getId().toString())
				.province(merchant.getProvince())
				.city(merchant.getCity())
				.district(merchant.getDistrict())
				.tags((am!=null)?am.getTags():mAncestor.getTags())
				.topAncestor(mAncestor)
				.build());
	
	}

}
