package io.chainmind.myriadapi.domain.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@Builder
public class OrgDTO implements Comparable{
	private String id;
	private String name;
	private String shortName;
	private String code;
	private String licenseNo;
	private String address;
	private String phone;

	@Override
	public int compareTo(Object o) {
		OrgDTO dto = (OrgDTO)o;
		return this.id.compareTo(dto.getId());
	}
}
