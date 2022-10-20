package com.nfinity.util;

import java.math.BigDecimal;
import java.util.Objects;

public class BigDecimalUtil {
    public static BigDecimal stripTrailingZeros(BigDecimal src){
        if(Objects.nonNull(src)){
            return src.stripTrailingZeros();
        }
        return null;
    }
}
