package io.chainmind.myriad.domain.dto;

import lombok.*;

@Getter
@Setter
@Builder
public class RegistrationResponse {
	// Myriad ID used to identify the client
	private String id;
	
	private String walletId;
	
	private String name;
	
	private String description;
	
	private String homeDomain;
	
	private String status;
}
