package io.chainmind.myriad.domain.dto.voucher.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyResponse {

    public static CurrencyResponse USD = new CurrencyResponse("USD",2);
    public static CurrencyResponse RMB = new CurrencyResponse("RMB", 2);

    private String symbol;
    private int precision;
}
