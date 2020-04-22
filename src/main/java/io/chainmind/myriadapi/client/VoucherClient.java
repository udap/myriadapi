package io.chainmind.myriadapi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import io.chainmind.myriad.domain.common.VoucherType;
import io.chainmind.myriad.domain.dto.PaginatedResponse;
import io.chainmind.myriad.domain.dto.voucher.VoucherListItem;
import io.chainmind.myriad.domain.dto.voucher.VoucherResponse;

@FeignClient(name = "myriad-voucher",url="${myriad.ribbon.listOfServers}")
public interface VoucherClient {
	@GetMapping(value = "/vouchers")
	PaginatedResponse<VoucherListItem> list(
            @PageableDefault(size = 20, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String ownerId,
            @RequestParam(required = false) String campaignId,
            @RequestParam(required = false) String publisherId,
            @RequestParam(required = false) String merchantId,
            @RequestParam(required = false, defaultValue="COUPON") VoucherType type,
            @RequestParam(required = false)String searchTxt);
	
    @GetMapping("/{id}")
    public VoucherResponse findById(@PathVariable(name = "id") String voucherId);
    
}
