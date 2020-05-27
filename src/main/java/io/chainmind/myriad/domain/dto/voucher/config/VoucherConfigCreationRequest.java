package io.chainmind.myriad.domain.dto.voucher.config;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import io.chainmind.myriad.domain.common.VoucherType;
import io.chainmind.myriad.domain.dto.validation.VoucherConfigValid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
		@JsonSubTypes.Type(value = CouponVoucherConfigCreationRequest.class, name = VoucherType.Constants.COUPON)
//		@JsonSubTypes.Type(value = Cat.class, name = "Cat")
})
public abstract class VoucherConfigCreationRequest implements Serializable {
	private static final long serialVersionUID = -8623092472181827710L;

	@NotBlank(groups = { VoucherConfigValid.Creator.class })
	private String reqOrg;

	@NotBlank(groups = { VoucherConfigValid.Creator.class, VoucherConfigValid.Updater.class })
	private String reqUser;

	@Size(max = 4000)
	private String description;

	private boolean multiple = false;

	@Size(max = 8)
	private String code;

	private String coverImg;// base64 image

	private Boolean authorizationRequired;

	@NotNull
	private VoucherType type;

	// voucher config configuration
	@Valid
	private CodeConfigCreationRequest codeConfig;

}
