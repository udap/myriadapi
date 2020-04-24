package io.chainmind.myriad.domain.dto.customer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressResponse {
	private String street;

	private String city;

	private String state;
	
	private String country;

	private String postalCode;

}
