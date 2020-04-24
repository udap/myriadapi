package io.chainmind.myriad.domain.dto.voucher;

import io.chainmind.myriad.domain.common.VoucherType;
import io.chainmind.myriad.domain.dto.CreateEntityResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleVoucherResponse extends CreateEntityResponse {
	private String code;
	private VoucherType type;
}
