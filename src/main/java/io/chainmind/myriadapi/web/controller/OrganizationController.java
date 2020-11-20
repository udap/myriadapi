package io.chainmind.myriadapi.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.chainmind.myriad.domain.dto.PaginatedResponse;
import io.chainmind.myriadapi.domain.CodeName;
import io.chainmind.myriadapi.domain.RequestUser;
import io.chainmind.myriadapi.domain.dto.Code;
import io.chainmind.myriadapi.domain.dto.OrgDTO;
import io.chainmind.myriadapi.domain.entity.Organization;
import io.chainmind.myriadapi.domain.exception.ApiException;
import io.chainmind.myriadapi.service.OrganizationService;
import io.chainmind.myriadapi.utils.CommonUtils;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {
	@Autowired
	private OrganizationService orgService;
	@Autowired
	private RequestUser requestUser;

	private OrgDTO queryByCode(Code code) {
		Code oCode = CommonUtils.uniqueCode(requestUser.getAppOrg().getId().toString(), code);
		Organization org = orgService.findByCode(oCode.getValue(), oCode.getName());
		if (Objects.isNull(org) || !org.isActive())
			throw new ApiException(HttpStatus.NOT_FOUND, "organization.notFound");
		// verify if the org is either the registered org or a subsidary of the registered org
		Organization topAncestor = orgService.findTopAncestor(org);
		if (!Objects.equals(topAncestor.getId(), requestUser.getAppOrg().getId()))
			throw new ApiException(HttpStatus.UNAUTHORIZED,"organization.unauthorized");
		return OrgDTO.builder()
				.id(org.getId().toString())
				.name(org.getFullName())
				.shortName(org.getName())
				.phone(org.getPhone())
				.address(org.getFullAddress())
				.licenseNo(org.getLicenseNo())
				.build();
	}
	/**
	 * Query organizations by codes.
	 * Caller can only query organizations that is either the registered organization (associated to the app id)
	 * or a subsidiary of the registered organization
	 * @param codes a comma separated name:value string.
	 */
	@GetMapping("/search")
	public List<OrgDTO> findAllByCodes(@RequestParam(name="codes") String codes) {
		List<Code> parsedCodes = CommonUtils.parseMixedCode(codes);
		if (parsedCodes.isEmpty())
			throw new ApiException(HttpStatus.BAD_REQUEST, "code.illegal");
		List<OrgDTO> results = new ArrayList<OrgDTO>();
		parsedCodes.forEach(oCode->results.add(queryByCode(oCode)));
		return results;
	}
	
	@GetMapping("/search/{code}")
	public OrgDTO findByCode(@PathVariable String code) {
		List<Code> parsedCodes = CommonUtils.parseMixedCode(code);
		if (parsedCodes.size() != 1)
			throw new ApiException(HttpStatus.BAD_REQUEST,"code.illegal");
		return queryByCode(parsedCodes.get(0));
	}
	
	@GetMapping("/{id}")
	public OrgDTO findById(@PathVariable String id) {
		Organization org = orgService.findById(Long.valueOf(id));
		if (Objects.isNull(org) || !org.isActive())
			throw new ApiException(HttpStatus.NOT_FOUND, "organization.notFound");
		Organization topAncestor = orgService.findTopAncestor(org);
		if (!Objects.equals(topAncestor.getId(), requestUser.getAppOrg().getId()))
			throw new ApiException(HttpStatus.UNAUTHORIZED,"organization.unauthorized");
		return OrgDTO.builder()
				.id(org.getId().toString())
				.name(org.getFullName())
				.shortName(org.getName())
				.phone(org.getPhone())
				.address(org.getFullAddress())
				.licenseNo(org.getLicenseNo())
				.build();
	}
	
	@GetMapping("/{id}/subsidiaries")
	public PaginatedResponse<OrgDTO> querySubsidiaries(@PathVariable String id,
			@RequestParam(name="directOnly", required=false, defaultValue="true") Boolean directOnly,
            @RequestParam(name="page", required=false, defaultValue="0") int page,
            @RequestParam(name="size", required=false, defaultValue="20") int size,
            @RequestParam(name="sort", required=false, defaultValue="createdAt:desc") String sort
            ) {
		Organization org = orgService.findByCode(id, CodeName.ID);
		if (Objects.isNull(org) || !org.isActive())
			throw new ApiException(HttpStatus.NOT_FOUND, "organization.notFound");
		Organization topAncestor = orgService.findTopAncestor(org);
		if (!Objects.equals(topAncestor.getId(), requestUser.getAppOrg().getId()))
			throw new ApiException(HttpStatus.UNAUTHORIZED,"organization.unauthorized");
		
//    	PageRequest pageRequest = PageRequest.of(page, size, CommonUtils.parseSort(sort));
    	
    	// convert to PaginatedResponse
    	PaginatedResponse<OrgDTO> result = new PaginatedResponse<OrgDTO>();
//    	result.setPage(aPage.getNumber());
//    	result.setSize(aPage.getSize());
//    	result.setTotal(aPage.getTotalPages());
//    	result.setTotalElements(aPage.getTotalElements());
		
		return result;
	}
//	
//	@GetMapping("/{id}/merchants")
//	public PaginatedResponse<OrgDTO> queryMerchants(@PathVariable String id) {
//		
//	}

}
