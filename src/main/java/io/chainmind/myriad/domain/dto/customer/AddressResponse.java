package io.chainmind.myriad.domain.dto.customer;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddressResponse {
	private String street;

	private String city;

	private String state;
	
	private String country;

	private String postalCode;

}
