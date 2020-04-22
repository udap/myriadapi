package io.chainmind.myriad.domain.dto.campaign;

import java.time.LocalDate;

import io.chainmind.myriad.domain.dto.IdResponse;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class CampaignListItemResponse extends IdResponse {
	private String owner;
	private String name;
	private String description;
	private String category;
	private String type;
	private String status;
	private LocalDate effective;
	private LocalDate expiry;
	private String createdBy;


}
