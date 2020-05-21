package io.chainmind.myriad.domain.dto.voucher;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleVoucherItem {
	private String id;
	private String code;
	private String issuer;
}
