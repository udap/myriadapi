package io.chainmind.myriad.domain.dto.campaign;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

import javax.validation.Valid;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import io.chainmind.myriad.domain.common.CampaignType;
import io.chainmind.myriad.domain.dto.validation.CampaignValid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = CreateVoucherCampaignRequest.class, name = CampaignType.Constants.VOUCHER)
//		@JsonSubTypes.Type(value = Cat.class, name = "Cat")
})
public abstract class CreateCampaignRequest implements Serializable {
	private static final long serialVersionUID = -3162305754697348558L;

	/*
	 * @ApiModelProperty(notes = "Unique identifier of the Contact.", example = "1",
	 * required = true, position = 0)
	 */

	@NotBlank
	private String reqOrg;

	@NotBlank
	private String reqUser;

	/*
	 * @ApiModelProperty(value="创建活动的机构id")
	 * 
	 * @NotNull(groups = CampaignValid.Creator.class) private String owner;
	 */

	@NotBlank(groups = CampaignValid.Creator.class)
	@Size(max = 64)
	private String name;

	@Size(max = 255)
	private String description;

	@NotNull(groups = CampaignValid.Creator.class)
	private CampaignType type;

	@Size(max = 20)
	private String category;

	@NotNull(groups = CampaignValid.Creator.class)
	@FutureOrPresent
	private LocalDate effective;

	@NotNull(groups = CampaignValid.Creator.class)
	@FutureOrPresent
	private LocalDate expiry;

	@Size(max = 255)
	private String url;

	@Valid
	@Size(max = 64)
	private Map<String, Object> metadata = new TreeMap<>();

}
