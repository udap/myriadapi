package io.chainmind.myriadapi.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.chainmind.myriad.domain.common.CampaignStatus;
import io.chainmind.myriad.domain.common.ParticipantType;
import io.chainmind.myriad.domain.common.PartyType;
import io.chainmind.myriad.domain.dto.campaign.CampaignListItemResponse;
import io.chainmind.myriadapi.client.VoucherClient;
import io.chainmind.myriadapi.domain.CodeType;
import io.chainmind.myriadapi.domain.entity.Account;
import io.chainmind.myriadapi.domain.entity.Organization;
import io.chainmind.myriadapi.domain.exception.ApiException;
import io.chainmind.myriadapi.service.AccountService;
import io.chainmind.myriadapi.service.AuthorizedMerchantService;
import io.chainmind.myriadapi.service.OrganizationService;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {
	@Autowired
	private VoucherClient voucherClient;
	@Autowired
	private AccountService accountService;
	@Autowired
	private OrganizationService orgService;
	@Autowired
	private AuthorizedMerchantService authorizedMerchantService;
	
    @GetMapping
    public Page<CampaignListItemResponse> listCampaigns(
            @PageableDefault(size = 20, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String partyId, // party id
            @RequestParam(required = false, defaultValue="HOST") PartyType partyType,
            @RequestParam(required = false, defaultValue="ID") CodeType partyIdType, 
            @RequestParam(required = false) String participantId, // the account that participated in the campaign
            @RequestParam(required = false, defaultValue="OWNER") ParticipantType participantType,
            @RequestParam(required = false, defaultValue="ID") CodeType participantIdType,
            @RequestParam(required = false) CampaignStatus status,            
            @RequestParam(required = false)String searchTxt) {
    	
    	if (StringUtils.hasText(participantId)) {
    		Account account = accountService.findByCode(participantId, participantIdType);
    		if (account == null)
    			throw new ApiException(HttpStatus.NOT_FOUND, "account.notFound");
    		participantId = account.getId().toString();
    	}
    	
    	if (StringUtils.hasText(partyId)) {
	    	partyId = authorizedMerchantService.getId(partyId, partyIdType);
    	}
    	
    	if (!StringUtils.hasText(participantId) && !StringUtils.hasText(partyId))
    		throw new ApiException(HttpStatus.BAD_REQUEST, "campaign.missingParams");
    	
    	Page<CampaignListItemResponse> page = voucherClient.listCampaigns(pageable, partyId, partyType, 
    			participantId, participantType, status, searchTxt);
    	
    	for(CampaignListItemResponse c : page.getContent()) {
    		Account account = accountService.findById(c.getCreatedBy());
    		if (account != null)
    			c.setCreatedBy(account.getName());
    		Organization org = orgService.findById(Long.valueOf(c.getOwner()));
    		if (org != null)
    			c.setOwner(org.getName());
    	}    	
    	
    	return page;
    }
}
