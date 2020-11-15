package io.chainmind.myriadapi.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.chainmind.myriadapi.domain.CodeType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Code {
	// id can be a comma-separated string representing multiple codes if type is MIXED, however, we cannot mix
	// account code with organizational code
	// e.g., "SOURCE_ID:153845697938,CELLPHONE:18621996666", which is a valid mixed id representing two account codes,
	// ,however, "SOURCE_ID:153845697938,LICENSE:12345", which is an invalid mixed id
	@NotBlank
	private String id;
	@NotNull
	private CodeType type;
}
