package io.chainmind.myriad.domain.dto.voucher;

import io.chainmind.myriad.domain.common.VoucherType;
import io.chainmind.myriad.domain.dto.CreateEntityResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class SimpleVoucherResponse extends CreateEntityResponse {
	private String code;
	private VoucherType type;
}
