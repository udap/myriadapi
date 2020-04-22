package io.chainmind.myriad.domain.dto.voucher.config;

import java.io.Serializable;

import io.chainmind.myriad.domain.common.DiscountType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiscountResponse implements Serializable {
    private static final long serialVersionUID = 6381188576312239633L;

    private DiscountType type;
    // discount by the valueOff, e.g., 20 for 20% if type is PERCENT, or 20 for 20 dollar off
    // if type is AMOUNT, or 20 for 20 hours training course if type is UNIT
    private Integer valueOff;

    private Integer amountLimit;
    // unit type, eg. Hour, Count
    private String unitType;


}
