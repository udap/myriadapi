package io.chainmind.myriad.domain.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class CreateEntityResponse {
	// entity identifier
    private String id;
    // entity creation time
	private LocalDateTime createdAt;
	private String createdBy;
}
