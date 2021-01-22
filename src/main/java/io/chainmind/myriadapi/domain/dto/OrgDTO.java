package io.chainmind.myriadapi.domain.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@Builder
public class OrgDTO {
	private String id;
	private String name;
	private String shortName;
	private String code;
	private String licenseNo;
	private String address;
	private String phone;
}
