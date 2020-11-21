package io.chainmind.myriadapi.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.chainmind.myriadapi.domain.CodeName;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Code {
	// value can be a comma-separated string representing multiple codes if type is MIXED, however, we cannot mix
	// account code with organizational code
	// e.g., "SOURCE_ID:153845697938,CELLPHONE:18621996666", which is a valid mixed value representing two account codes,
	// ,however, "SOURCE_ID:153845697938,LICENSE:12345", which is an invalid mixed value
	@NotBlank
	private String value;
	@NotNull
	private CodeName name;
}
