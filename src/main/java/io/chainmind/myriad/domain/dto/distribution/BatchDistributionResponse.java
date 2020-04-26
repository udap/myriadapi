package io.chainmind.myriad.domain.dto.distribution;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BatchDistributionResponse {
	// number of customers 
	private int customerCount;
	// number of vouchers distributed to customers
	private int voucherCount;
}
