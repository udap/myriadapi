package io.chainmind.myriadapi.web.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.chainmind.myriad.domain.common.EffectiveStatus;
import io.chainmind.myriad.domain.common.ParticipantType;
import io.chainmind.myriad.domain.common.PartyType;
import io.chainmind.myriad.domain.dto.PaginatedResponse;
import io.chainmind.myriad.domain.dto.campaign.CampaignListItem;
import io.chainmind.myriad.domain.dto.campaign.CampaignResponse;
import io.chainmind.myriadapi.client.VoucherClient;
import io.chainmind.myriadapi.domain.CodeName;
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
	
	/**
	 * Query the campaigns related to the given query conditions
	 * @param page
	 * @param size
	 * @param sort
	 * @param hostId the id of the host organization, default to current registered organization
	 * @param hostIdType the id type used to define the meaning of the hostId
	 * @param participantId the id of the participant account who either creates the campaigns or participates in the campaigns
	 * @param participantType the 
	 * @param participantIdType
	 * @param status
	 * @param searchTxt
	 * @return
	 */
    @GetMapping
    public PaginatedResponse<CampaignListItem> listCampaigns(
            @RequestParam(name="page", required=false, defaultValue="0") int page,
            @RequestParam(name="size", required=false, defaultValue="20") int size,
            @RequestParam(name="sort", required=false, defaultValue="createdAt:desc") String sort,    		
            @RequestParam(required = false) String host, // the host organization id, default to the registered app org
            @RequestParam(required = false) String createdBy, // the account id that created the campaigns
            @RequestParam(required = false, defaultValue="ACTIVE") EffectiveStatus status,            
            @RequestParam(required = false)String searchTxt) {

    	PageRequest pageRequest = PageRequest.of(page, size, CommonUtils.parseSort(sort));
		log.debug("GET /api/campaigns: sorts: {}", pageRequest.getSort());
		// validate the host party
    	String hostParty = requestUser.getAppOrg().getId().toString();
    	if (StringUtils.hasText(host)) {
        	// ensure party is in the management scope of the App Org
    		Organization party = orgService.findByCode(host, CodeName.ID);
    		Organization topAncestor = orgService.findTopAncestor(party);
    		if (!Objects.equals(requestUser.getAppOrg().getId(), topAncestor.getId()))
    			throw new ApiException(HttpStatus.UNAUTHORIZED, "organization.unauthorized");
    		hostParty = party.getId().toString();
	    	
    	} 

    	if (StringUtils.hasText(createdBy)) {
    		Account account = accountService.findByCode(createdBy, CodeName.ID);
    		if (account == null)
    			throw new ApiException(HttpStatus.NOT_FOUND, "account.notFound");
    		createdBy = account.getId().toString();
    	}
    	
    	// query campaigns
    	Page<CampaignListItem> aPage = voucherClient.listCampaigns(pageRequest, hostParty, PartyType.HOST, 
    			createdBy, ParticipantType.OWNER, status, searchTxt);
    
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
    
    /**
     * Query a campaign given its id. If the campaign is not in the scope of current app, a HTTP 403 error is returned
     * @param campaignId campaign id
     * @return a CampaignResponse or a HTTP 403 response
     */
    @GetMapping("/{id}")
    public CampaignResponse getCampaign(@PathVariable(name="id")String campaignId) {
    	CampaignResponse campaign = voucherClient.getCampaign(campaignId);
    	// ensure campaign is in the scope of the app
		Organization org = orgService.findById(Long.valueOf(campaign.getOwner()));
		Organization topAncestor = orgService.findTopAncestor(org);
		if (!Objects.equals(requestUser.getAppOrg().getId(), topAncestor.getId()))
			throw new ApiException(HttpStatus.UNAUTHORIZED, "organization.unauthorized");
		campaign.setTotalSupply(voucherClient.countTotalSupply(campaignId));
    	return campaign;
    }
}
