package io.chainmind.myriad.domain.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEntityResponse {
	// entity identifier
    private String id;
    // entity creation time
	private LocalDateTime createdAt;
	private String createdBy;
}
