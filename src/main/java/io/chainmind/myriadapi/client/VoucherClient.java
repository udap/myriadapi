package io.chainmind.myriadapi.client;

import javax.validation.Valid;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.chainmind.myriad.domain.common.EffectiveStatus;
import io.chainmind.myriad.domain.common.ParticipantType;
import io.chainmind.myriad.domain.common.PartyType;
import io.chainmind.myriad.domain.common.VoucherType;
import io.chainmind.myriad.domain.dto.PaginatedResponse;
import io.chainmind.myriad.domain.dto.campaign.CampaignListItemResponse;
import io.chainmind.myriad.domain.dto.distribution.BatchDistributionRequest;
import io.chainmind.myriad.domain.dto.distribution.BatchDistributionResponse;
import io.chainmind.myriad.domain.dto.distribution.DistributeVoucherRequest;
import io.chainmind.myriad.domain.dto.distribution.DistributeVoucherResponse;
import io.chainmind.myriad.domain.dto.redemption.ConfirmRedemptionRequest;
import io.chainmind.myriad.domain.dto.redemption.ConfirmRedemptionResponse;
import io.chainmind.myriad.domain.dto.redemption.CreateRedemptionRequest;
import io.chainmind.myriad.domain.dto.redemption.CreateRedemptionResponse;
import io.chainmind.myriad.domain.dto.voucher.BatchTransferRequest;
import io.chainmind.myriad.domain.dto.voucher.BatchTransferResponse;
import io.chainmind.myriad.domain.dto.voucher.QualifyRequest;
import io.chainmind.myriad.domain.dto.voucher.QualifyResult;
import io.chainmind.myriad.domain.dto.voucher.TransferVoucherRequest;
import io.chainmind.myriad.domain.dto.voucher.TransferVoucherResponse;
import io.chainmind.myriad.domain.dto.voucher.UsageStatus;
import io.chainmind.myriad.domain.dto.voucher.VoucherListItem;
import io.chainmind.myriad.domain.dto.voucher.VoucherResponse;

@FeignClient(name = "voucher-service")
public interface VoucherClient {
	
    @GetMapping(value="/campaigns")
    public Page<CampaignListItemResponse> listCampaigns(
            @PageableDefault(size = 20, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String partyId, // party id
            @RequestParam(required = false, defaultValue="HOST")PartyType partyType,            
            @RequestParam(required = false) String participantId, // the account that participated in the campaign
            @RequestParam(required = false, defaultValue="OWNER") ParticipantType participantType,
            @RequestParam(required = false) EffectiveStatus status,            
            @RequestParam(required = false)String searchTxt);

	
	@GetMapping(value = "/vouchers")
	PaginatedResponse<VoucherListItem> queryVouchers(
            @PageableDefault(size = 20, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String ownerId,
            @RequestParam(required = false) String campaignId,
            @RequestParam(required = false) String issuerId,
            @RequestParam(required = false) String merchantId,
            @RequestParam(required = false, defaultValue="COUPON") VoucherType type,
            @RequestParam(required = false) UsageStatus status,
            @RequestParam(required = false, defaultValue="false") Boolean excludeIssued,
            @RequestParam(required = false)String searchTxt);
	
    @GetMapping("/vouchers/{id}")
    VoucherResponse findVoucherById(@PathVariable(name = "id") String voucherId);
    
    @PutMapping("/vouchers/{id}/transfer")
    TransferVoucherResponse transferVoucher(@PathVariable(name="id")String voucherId, 
    		@Valid @RequestBody TransferVoucherRequest req);
    
    @PostMapping("/vouchers/batchTransfer")
    BatchTransferResponse batchTransfer(@RequestBody @Valid BatchTransferRequest req);
    
    @PostMapping("/vouchers/{id}/qualify")
    QualifyResult qualify(@PathVariable(name = "id") String voucherId, @Valid @RequestBody QualifyRequest req);

	@PostMapping("/distributions")
	DistributeVoucherResponse distributeVoucher(@Valid @RequestBody DistributeVoucherRequest req);
	
	@PostMapping("/distributions/batch")
	BatchDistributionResponse distributeVouchers(@Valid @RequestBody BatchDistributionRequest req);
    
    @PostMapping(value="/redemptions")
	CreateRedemptionResponse createRedemption(@Valid @RequestBody CreateRedemptionRequest req);

	@PutMapping(value="/redemptions")
	ConfirmRedemptionResponse confirmRedemption(@Valid @RequestBody ConfirmRedemptionRequest req);


}
