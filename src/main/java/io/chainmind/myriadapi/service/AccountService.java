package io.chainmind.myriadapi.service;

import javax.transaction.Transactional;

import io.chainmind.myriadapi.domain.CodeName;
import io.chainmind.myriadapi.domain.entity.Account;

public interface AccountService {
	Account findByCode(String code, CodeName codeType);
	
	Account findById(String id);
	
	@Transactional
	Account register(String code, CodeName codeType);
}
