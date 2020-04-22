package io.chainmind.myriad.domain.dto.voucher;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.chainmind.myriad.domain.common.VoucherType;
import io.chainmind.myriad.domain.dto.campaign.RuleRequest;
import io.chainmind.myriad.domain.dto.voucher.config.VoucherConfigCreationRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateVoucherRequest {

	@NotBlank
	private String reqUser;

	@NotBlank
	private String reqOrg;

	@NotNull
	private VoucherType type;

	@Size(max = 20)
	private String category;

	@NotNull
	@FutureOrPresent
	private LocalDate effective;

	@NotNull
	@FutureOrPresent
	private LocalDate expiry;

	@Size(max = 64)
	private Map<String, Object> metadata;

	@NotNull
	@Valid
	private VoucherConfigCreationRequest voucherConfig;

	@Valid
	private Set<RuleRequest> rules = new HashSet<>();

}
