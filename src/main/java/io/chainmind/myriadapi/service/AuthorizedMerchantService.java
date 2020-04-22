package io.chainmind.myriadapi.service;

import io.chainmind.myriadapi.domain.CodeType;

public interface AuthorizedMerchantService {
	String getId(String code, CodeType codeType);
}
