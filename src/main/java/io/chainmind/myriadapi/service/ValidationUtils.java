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
			AuthorizedMerchant am, AuthorizedMerchant amAncestor, boolean existsInDescendants) {
		// ensure merchant and its ancestor cannot be null
		assert(merchant != null && ancestor != null);
		// neither merchant nor its top ancestor is authorized 
		if (Objects.isNull(am) && Objects.isNull(amAncestor) && !existsInDescendants)
			return Optional.empty();

		Merchant mAncestor = null;
		if (Objects.nonNull(amAncestor) && !Objects.equals(merchant.getId(), ancestor.getId()))
			mAncestor = Merchant.builder()
				.id(ancestor.getId().toString())
				.build();
		String tags = null;
		if (Objects.nonNull(am))
			tags = am.getTags();
		else if (Objects.nonNull(mAncestor))
			tags = mAncestor.getTags();
		return Optional.of(Merchant.builder()
				.id(merchant.getId().toString())
				.province(merchant.getProvince())
				.city(merchant.getCity())
				.district(merchant.getDistrict())
				.tags(tags)
				.topAncestor(mAncestor)
				.build());
	
	}

}
