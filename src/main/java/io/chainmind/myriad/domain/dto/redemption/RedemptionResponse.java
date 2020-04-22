package io.chainmind.myriad.domain.dto.redemption;

import java.time.LocalDateTime;
import java.util.Map;

import io.chainmind.myriad.domain.common.RedemptionStatus;
import io.chainmind.myriad.domain.dto.IdResponse;
import io.chainmind.myriad.domain.dto.voucher.VoucherSummary;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class RedemptionResponse extends IdResponse {

	private VoucherSummary voucher;
	// the merchant that redeems the voucher
	private String merchantId;
	// the customer that redeems the voucher
	private String customerId;
	private RedemptionStatus status;
	// extra data
	private Map<String, Object> metadata;
	private String errorCode;
	// the account that distributes the voucher
	private String createdBy;
	private LocalDateTime createdAt;

}
