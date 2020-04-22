package io.chainmind.myriad.domain.dto.voucher;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import io.chainmind.myriad.domain.common.VoucherStatus;
import io.chainmind.myriad.domain.dto.IdResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class VoucherSummary extends IdResponse {
	private LocalDateTime updatedAt;
	private String issuer;// 发行券的机构
	private String code;
	private String category;
	private LocalDate effective;
	private LocalDate expiry;
	private Integer redeemedQuantity;
	private String authorizationCode;
	private VoucherStatus status;
	private Map<String, Object> metadata;
	private String owner;
}
