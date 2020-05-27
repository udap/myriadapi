package io.chainmind.myriadapi.domain.dto;

import java.util.List;
import java.util.Set;

import io.chainmind.myriad.domain.dto.rule.RuleResponse;
import io.chainmind.myriad.domain.dto.voucher.VoucherSummary;
import io.chainmind.myriad.domain.dto.voucher.config.SimpleVoucherConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoucherDetailsResponse extends VoucherSummary {
    private SimpleVoucherConfig config;

    private Set<RuleResponse> rules;
    
    // where the vouchers can be redeemed
    private List<OrgDTO> merchants;

}
