package io.chainmind.myriadapi.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.chainmind.myriadapi.domain.CodeType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Code {
	@NotBlank
	private String id;
	@NotNull
	private CodeType type;
}
