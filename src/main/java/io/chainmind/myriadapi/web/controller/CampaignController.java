package io.chainmind.myriadapi.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.chainmind.myriad.domain.common.EffectiveStatus;
import io.chainmind.myriad.domain.common.ParticipantType;
import io.chainmind.myriad.domain.common.PartyType;
import io.chainmind.myriad.domain.dto.PaginatedResponse;
import io.chainmind.myriad.domain.dto.campaign.CampaignListItem;
import io.chainmind.myriadapi.client.VoucherClient;
import io.chainmind.myriadapi.domain.CodeType;
import io.chainmind.myriadapi.domain.RequestUser;
import io.chainmind.myriadapi.domain.entity.Account;
import io.chainmind.myriadapi.domain.entity.Organization;
import io.chainmind.myriadapi.domain.exception.ApiException;
import io.chainmind.myriadapi.service.AccountService;
import io.chainmind.myriadapi.service.OrganizationService;
import io.chainmind.myriadapi.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	private RequestUser requestUser;
	
    @GetMapping
    public PaginatedResponse<CampaignListItem> listCampaigns(
            @RequestParam(name="page", required=false, defaultValue="0") int page,
            @RequestParam(name="size", required=false, defaultValue="20") int size,
            @RequestParam(name="sort", required=false, defaultValue="createdAt:desc") String sort,    		
//            @PageableDefault(size = 20, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String partyId, // party id
            @RequestParam(required = false, defaultValue="HOST") PartyType partyType,
            @RequestParam(required = false, defaultValue="ID") CodeType partyIdType, 
            @RequestParam(required = false) String participantId, // the account that participated in the campaign
            @RequestParam(required = false, defaultValue="OWNER") ParticipantType participantType,
            @RequestParam(required = false, defaultValue="ID") CodeType participantIdType,
            @RequestParam(required = false) EffectiveStatus status,            
            @RequestParam(required = false)String searchTxt) {

    	PageRequest pageRequest = PageRequest.of(page, size, CommonUtils.parseSort(sort));
		log.debug("GET /api/campaigns: sorts: {}", pageRequest.getSort());

    	if (StringUtils.hasText(participantId)) {
    		Account account = accountService.findByCode(participantId, participantIdType);
    		if (account == null)
    			throw new ApiException(HttpStatus.NOT_FOUND, "account.notFound");
    		participantId = account.getId().toString();
    	}
    	// TODO: ensure party is in the management scope of the App Org
    	if (StringUtils.hasText(partyId)) {
	    	partyId = orgService.findByCode(partyId, partyIdType).getId().toString();
    	} else {
    		partyId = requestUser.getAppOrg().getId().toString();
    	}
    	
    	if (!StringUtils.hasText(participantId) && !StringUtils.hasText(partyId))
    		throw new ApiException(HttpStatus.BAD_REQUEST, "campaign.missingParams");
    	
    	Page<CampaignListItem> aPage = voucherClient.listCampaigns(pageRequest, partyId, partyType, 
    			participantId, participantType, status, searchTxt);
    
    	// convert to PaginatedResponse
    	PaginatedResponse<CampaignListItem> result = new PaginatedResponse<CampaignListItem>();
    	result.setPage(aPage.getNumber());
    	result.setSize(aPage.getSize());
    	result.setTotal(aPage.getTotalPages());
    	result.setTotalElements(aPage.getTotalElements());
    	for(CampaignListItem c : aPage.getContent()) {
    		Account account = accountService.findById(c.getCreatedBy());
    		if (account != null)
    			c.setCreatedBy(account.getName());
    		Organization org = orgService.findById(Long.valueOf(c.getOwner()));
    		if (org != null)
    			c.setOwner(org.getName());
    		result.getEntries().add(c);
    	} 
    	
    	return result;
    }
}
