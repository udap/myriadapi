package io.chainmind.myriadapi.domain;

import io.chainmind.myriadapi.domain.entity.Organization;
import lombok.Data;

@Data
public class RequestOrg {
	// app registration id
	private String appId;
	// the top-level organization that owns the appid
	private Organization appOrg;
}
