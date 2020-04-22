package io.chainmind.myriad.domain.dto.voucher;

import io.chainmind.myriad.domain.dto.voucher.config.VoucherConfigResponse;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class VoucherListItem extends VoucherSummary {
	private VoucherConfigResponse config;
}
