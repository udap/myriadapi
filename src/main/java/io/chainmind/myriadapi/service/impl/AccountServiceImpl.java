package io.chainmind.myriadapi.service.impl;

import java.util.Date;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import io.chainmind.myriadapi.CacheConfiguration;
import io.chainmind.myriadapi.domain.CodeType;
import io.chainmind.myriadapi.domain.RequestUser;
import io.chainmind.myriadapi.domain.entity.Account;
import io.chainmind.myriadapi.domain.exception.ApiException;
import io.chainmind.myriadapi.persistence.repository.AccountRepository;
import io.chainmind.myriadapi.service.AccountService;
import io.chainmind.myriadapi.utils.CommonUtils;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository accountRepo;
	@Autowired
	private RequestUser requestOrg;
	
	@Cacheable(value = CacheConfiguration.ACCOUNT_BY_CODE_CACHE, unless="#result == null")
	@Override
	public Account findByCode(String code, CodeType codeType) {
		String orgId = requestOrg.getAppOrg().getId().toString();
		Account account = null;
		if (CodeType.ID.equals(codeType)) {
			Optional<Account> accountOpt = accountRepo.findById(Long.valueOf(code));
			if (accountOpt.isPresent())
				account = accountOpt.get();
		} else if (CodeType.CELLPHONE.equals(codeType)) {
			account = accountRepo.findByCellphone(code);
		} else if (CodeType.EMAIL.equals(codeType)){
			account = accountRepo.findByEmail(code);
		} else if (CodeType.SOURCE_ID.equals(codeType)) {
			account = accountRepo.findByOrganizationIdAndSourceId(orgId,code);
		} else if (CodeType.NAME.equals(codeType)) {
			account = accountRepo.findByName(code);
		} else {
			throw new ApiException(HttpStatus.BAD_REQUEST, "account.invalidCodeType");
		}
		return account;
	}
		
	@Override
	public Account register(String code, CodeType codeType) {
		if (!CodeType.CELLPHONE.equals(codeType) && !CodeType.EMAIL.equals(codeType)
				&& !CodeType.SOURCE_ID.equals(codeType))
			throw new ApiException(HttpStatus.BAD_REQUEST, "非法的ID类型");

		Account account = new Account();
		account.setCreateTime(new Date());
		account.setEnabled(true);

		if (CodeType.CELLPHONE.equals(codeType)) {
			if (!CommonUtils.validateCellphone(code))
				throw new ApiException(HttpStatus.BAD_REQUEST, "registration.invalidPhoneNumber");
			account.setCellphone(code);			
			account.setName(code);
		}
		
		if (CodeType.EMAIL.equals(codeType)) {
			if (!CommonUtils.validateEmail(code))
				throw new ApiException(HttpStatus.BAD_REQUEST, "registration.invalidEmail");
			account.setEmail(code);
			account.setName(code);
		}
		
		if (CodeType.SOURCE_ID.equals(codeType)) {
			account.setSourceId(code);
			account.setName(DigestUtils.sha1Hex(requestOrg.getAppOrg().getId().toString() + "-" + code));
		}
		// IMPORTANT
		account.setOrganizationId(requestOrg.getAppOrg().getId().toString());
		
		return accountRepo.save(account);
	}

	@Cacheable(value = CacheConfiguration.ACCOUNT_CACHE, unless="#result == null")	
	@Override
	public Account findById(String id) {
		Optional<Account> account = accountRepo.findById(Long.valueOf(id));
		if (account.isPresent())
			return account.get();
		return null;
	}

}
