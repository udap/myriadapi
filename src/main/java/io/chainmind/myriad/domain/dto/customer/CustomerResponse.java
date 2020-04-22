package io.chainmind.myriad.domain.dto.customer;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerResponse {
	private String id;
	
	private String clientId;
	private String orgId;
	
	// unique
	private String sourceId;

	private String name;
	
	private String idCardNo;
	
	private String email;
	
	private String phone;
	
	private String wallet;
	
	private AddressResponse address;

	private Map<String, Object> metadata;

}
