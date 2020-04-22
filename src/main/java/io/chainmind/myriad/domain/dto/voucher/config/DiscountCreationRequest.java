package io.chainmind.myriad.domain.dto.voucher.config;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import io.chainmind.myriad.domain.common.DiscountType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = AmountDiscountCreationRequest.class, name = DiscountType.Constants.AMOUNT),
		@JsonSubTypes.Type(value = PercentDiscountCreationRequest.class, name = DiscountType.Constants.PERCENT),
		@JsonSubTypes.Type(value = UnitDiscountCreationRequest.class, name = DiscountType.Constants.UNIT) })
public abstract class DiscountCreationRequest implements Serializable {
	private static final long serialVersionUID = -1864524975172013415L;

	@NotNull
	private DiscountType type;

}
