package io.chainmind.myriad.domain.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class UpdateEntityResponse {
	// entity identifier
    private String id;
    // entity creation time
	private LocalDateTime updatedAt;
	private String updatedBy;
}
