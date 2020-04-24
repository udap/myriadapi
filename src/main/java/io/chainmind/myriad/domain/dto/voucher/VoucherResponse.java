package io.chainmind.myriad.domain.dto.voucher;

import java.util.List;
import java.util.Set;

import io.chainmind.myriad.domain.dto.rule.RuleResponse;
import io.chainmind.myriad.domain.dto.voucher.config.VoucherConfigResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoucherResponse extends VoucherSummary {

    private VoucherConfigResponse config;

    private Set<RuleResponse> rules;
    
    // where the vouchers can be redeemed
    private List<String> merchants;
}
