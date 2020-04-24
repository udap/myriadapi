package io.chainmind.myriad.domain.dto.voucher;

import io.chainmind.myriad.domain.dto.voucher.config.VoucherConfigResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoucherListItem extends VoucherSummary {
	private int merchantCount;
	private VoucherConfigResponse config;
}
