package io.chainmind.myriad.domain.dto.campaign;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import io.chainmind.myriad.domain.dto.IdResponse;
import io.chainmind.myriad.domain.dto.promotion.TierResponse;
import io.chainmind.myriad.domain.dto.rule.ValidationDTO;
import io.chainmind.myriad.domain.dto.voucher.config.VoucherConfigResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CampaignResponse extends IdResponse {

	private LocalDateTime updatedAt;
	private String createdBy;
	private String owner;
	private String name;
	private String type;
	private String subType;
	private LocalDate effective;
	private LocalDate expiry;
	private String description;
	private String url;
	private String category;
	// number of vouchers to be issued
	private int plannedSupply;
	// number of vouchers issued
	private int totalSupply;
	// allow or disallow additional issuance
	private boolean autoUpdate;
	// distribution method
	private String distMethod;
	// limit per account
	private int distLimit;
	private String status;
	// additional metadata
	private Map<String, Object> metadata;

	private Set<CampaignPartyResponse> parties;

	// PROMOTION 的字段
	private Set<TierResponse> tiers;

	// VOUCHER 的字段
	private Set<ValidationDTO> rules;
	private VoucherConfigResponse voucherConfig;

}
