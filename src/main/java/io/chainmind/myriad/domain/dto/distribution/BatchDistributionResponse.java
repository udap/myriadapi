package io.chainmind.myriad.domain.dto.distribution;

import io.chainmind.myriadapi.domain.dto.BatchStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BatchDistributionResponse {
	// number of customers 
	private int customerCount;
	// number of vouchers distributed to customers
	private int voucherCount;
	
	private BatchStatus status;
	private String msg;
}
