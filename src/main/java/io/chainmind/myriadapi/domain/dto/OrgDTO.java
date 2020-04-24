package io.chainmind.myriadapi.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrgDTO {
	private String uid;
	private String name;
	private String address;
	private String phone;
}
