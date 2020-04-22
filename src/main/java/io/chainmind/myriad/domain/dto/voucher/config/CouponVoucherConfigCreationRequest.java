package io.chainmind.myriad.domain.dto.voucher.config;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.chainmind.myriad.domain.common.VoucherType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CouponVoucherConfigCreationRequest extends VoucherConfigCreationRequest {

    private static final long serialVersionUID = 4668631805005585750L;

    public CouponVoucherConfigCreationRequest() {
        this.setType(VoucherType.COUPON);
    }

    @NotNull
    @Valid
    private DiscountCreationRequest discount;






}
