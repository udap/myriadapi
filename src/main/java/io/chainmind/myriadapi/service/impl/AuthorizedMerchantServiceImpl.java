package io.chainmind.myriadapi.service.impl;

import io.chainmind.myriadapi.CacheConfiguration;
import io.chainmind.myriadapi.domain.CodeType;
import io.chainmind.myriadapi.domain.OrganizationStatus;
import io.chainmind.myriadapi.domain.entity.Organization;
import io.chainmind.myriadapi.domain.exception.ApiException;
import io.chainmind.myriadapi.persistence.repository.OrganizationRepository;
import io.chainmind.myriadapi.service.AuthorizedMerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AuthorizedMerchantServiceImpl implements AuthorizedMerchantService {
    @Autowired
    private OrganizationRepository orgRepo;

    @Cacheable(value = CacheConfiguration.MERCHANT_ID_CACHE, unless = "#result == null")
    @Override
    public String getId(String code, CodeType codeType) {
        if (codeType.equals(CodeType.ID))
            return code;
        Organization merchant = null;
        if (codeType.equals(CodeType.LICENSE))
            merchant = orgRepo.findTopByLicenseNo(code);
        else if (codeType.equals(CodeType.UPCODE))
            merchant = orgRepo.findTopByUpCode(code);
        else if (codeType.equals(CodeType.APCODE))
            merchant = orgRepo.findTopByApCode(code);
        else if (codeType.equals(CodeType.WPCODE))
            merchant = orgRepo.findTopByWpCode(code);
        else
            throw new ApiException(HttpStatus.BAD_REQUEST, "unknown code type");

        if (merchant == null || !merchant.getStatus().equals(OrganizationStatus.ACTIVE))
            throw new ApiException(HttpStatus.NOT_FOUND, "merchant.notFound");
        return merchant.getId().toString();
    }

}
