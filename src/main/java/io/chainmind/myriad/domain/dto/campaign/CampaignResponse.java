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
	private LocalDateTime createdBy;
	private String owner;
	private String name;
	private String description;
	private String category;
	private LocalDate effective;
	private LocalDate expiry;
	// number of vouchers to be issued
	private int totalSupply;
	// allow or disallow additional issuance
	private boolean autoUpdate;
	private String url;
	private Set<CampaignPartyResponse> parties;
	private String status;
	private Map<String, Object> metadata;
	private String type;

	// PROMOTION 的字段
	private Set<TierResponse> tiers;

	// VOUCHER 的字段
	private Set<ValidationDTO> rules;
	private VoucherConfigResponse voucherConfig;

}
