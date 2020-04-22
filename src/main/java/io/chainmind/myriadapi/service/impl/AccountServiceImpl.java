package io.chainmind.myriadapi.service.impl;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;

import io.chainmind.myriadapi.CacheConfiguration;
import io.chainmind.myriadapi.domain.CodeType;
import io.chainmind.myriadapi.domain.entity.Account;
import io.chainmind.myriadapi.domain.exception.ApiException;
import io.chainmind.myriadapi.persistence.repository.AccountRepository;
import io.chainmind.myriadapi.service.AccountService;
import io.chainmind.myriadapi.utils.CommonUtils;

public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository accountRepo;
	
	@Cacheable(value = CacheConfiguration.ACCOUNT_BY_CODE_CACHE, unless="#result == null")
	@Override
	public Account findByCode(String code, CodeType codeType) {
		Account account = null;
		if (CodeType.ID.equals(codeType)) {
			Optional<Account> accountOpt = accountRepo.findById(Long.valueOf(code));
			if (accountOpt.isPresent())
				account = accountOpt.get();
		} else if (CodeType.CELLPHONE.equals(codeType)) {
			account = accountRepo.findByCellphone(code);
		} else if (CodeType.EMAIL.equals(codeType)){
			account = accountRepo.findByName(code);
		} else {
			throw new ApiException(HttpStatus.BAD_REQUEST, "非法的ID类型");
		}
		return account;
	}
	
	@Override
	public Account register(String code, CodeType codeType) {
		if (!CodeType.CELLPHONE.equals(codeType) && !CodeType.EMAIL.equals(codeType))
			throw new ApiException(HttpStatus.BAD_REQUEST, "非法的ID类型");

		Account account = new Account();
		account.setName(code);
		account.setCreateTime(new Date());
		account.setEnabled(true);

		if (CodeType.CELLPHONE.equals(codeType)) {
			if (!CommonUtils.validateCellphone(code))
				throw new ApiException(HttpStatus.BAD_REQUEST, "非法的手机号码");
			account.setCellphone(code);			
		}
		
		if (CodeType.EMAIL.equals(codeType)) {
			if (!CommonUtils.validateEmail(code))
				throw new ApiException(HttpStatus.BAD_REQUEST, "非法的电子邮件地址");
		}
		return accountRepo.save(account);
	}

}