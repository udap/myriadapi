package io.chainmind.myriad.domain.dto.voucher;

import java.util.Set;

import io.chainmind.myriad.domain.dto.rule.RuleResponse;
import io.chainmind.myriad.domain.dto.voucher.config.VoucherConfigResponse;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class VoucherResponse extends VoucherSummary {

    private VoucherConfigResponse config;

    private Set<RuleResponse> rules;
}
