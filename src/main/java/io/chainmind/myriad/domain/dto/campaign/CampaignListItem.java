package io.chainmind.myriad.domain.dto.campaign;

import java.time.LocalDate;

import io.chainmind.myriad.domain.dto.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CampaignListItem extends IdResponse {
	private String owner;
	private String name;
	private String description;
	private String category;
	private String type;
	private String subType;
	private String status;
	private LocalDate effective;
	private LocalDate expiry;
	// number of vouchers to be issued
	private int plannedSupply;
	// number of vouchers issued
	private int totalSupply;
	// allow or disallow additional issuance
	private boolean autoUpdate;
	private String createdBy;

}
