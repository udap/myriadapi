package io.chainmind.myriadapi.service.impl;

import java.util.Date;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import io.chainmind.myriadapi.CacheConfiguration;
import io.chainmind.myriadapi.domain.CodeName;
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
	public Account findByCode(String code, CodeName codeType) {
//		String orgId = requestOrg.getAppOrg().getId().toString();
		Account account = null;
		if (CodeName.ID.equals(codeType)) {
			Optional<Account> accountOpt = accountRepo.findById(Long.valueOf(code));
			if (accountOpt.isPresent())
				account = accountOpt.get();
		} else if (CodeName.CELLPHONE.equals(codeType)) {
			account = accountRepo.findByCellphone(code);
		} else if (CodeName.EMAIL.equals(codeType)){
			account = accountRepo.findByEmail(code);
		} else if (CodeName.SOURCE_ID.equals(codeType)) {
			String[] parts = CommonUtils.parseCode(code);
			if (parts.length != 2)
				throw new ApiException(HttpStatus.BAD_REQUEST, "code.invalidCode");
			account = accountRepo.findByOrganizationIdAndSourceId(parts[0],parts[1]);
		} else if (CodeName.NAME.equals(codeType)) {
			account = accountRepo.findByName(code);
		} else {
			throw new ApiException(HttpStatus.BAD_REQUEST, "account.invalidCodeType");
		}
		return account;
	}
		
	@Override
	public Account register(String code, CodeName codeType) {
		if (!CodeName.CELLPHONE.equals(codeType) && !CodeName.EMAIL.equals(codeType)
				&& !CodeName.SOURCE_ID.equals(codeType))
			throw new ApiException(HttpStatus.BAD_REQUEST, "非法的ID类型");

		Account account = new Account();
		account.setCreateTime(new Date());
		account.setEnabled(true);

		if (CodeName.CELLPHONE.equals(codeType)) {
			if (!CommonUtils.validateCellphone(code))
				throw new ApiException(HttpStatus.BAD_REQUEST, "registration.invalidPhoneNumber");
			account.setCellphone(code);			
			account.setName(code);
		}
		
		if (CodeName.EMAIL.equals(codeType)) {
			if (!CommonUtils.validateEmail(code))
				throw new ApiException(HttpStatus.BAD_REQUEST, "registration.invalidEmail");
			account.setEmail(code);
			account.setName(code);
		}
		
		if (CodeName.SOURCE_ID.equals(codeType)) {
			String[] parts = CommonUtils.parseCode(code);
			if (parts.length != 2)
				throw new ApiException(HttpStatus.BAD_REQUEST, "code.invalid");
			account.setSourceId(parts[1]);
			account.setName(DigestUtils.sha1Hex(parts[0] + "-" + parts[1]));
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
