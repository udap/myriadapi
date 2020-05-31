package io.chainmind.myriad.domain.dto.voucher;

import io.chainmind.myriad.domain.dto.voucher.config.SimpleVoucherConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoucherListItem extends VoucherSummary {
	private Integer merchantCount;
	private SimpleVoucherConfig config;
}
