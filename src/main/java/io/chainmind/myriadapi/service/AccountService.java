package io.chainmind.myriadapi.service;

import javax.transaction.Transactional;

import io.chainmind.myriadapi.domain.CodeType;
import io.chainmind.myriadapi.domain.entity.Account;

public interface AccountService {
	Account findByCode(String code, CodeType codeType);
	
	@Transactional
	Account register(String code, CodeType codeType);
}
