package io.chainmind.myriadapi.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomerResponse {
	// account id
	private String id;
	// account name
    private String name;
    // customer real name
    private String realName;
    // account cellpone
    private String cellphone;
    // account email
    private String email;
    // account source id
    private String sourceId;
    
    private String coverImg;//BASE64-encoded image
    private String tags;
}
