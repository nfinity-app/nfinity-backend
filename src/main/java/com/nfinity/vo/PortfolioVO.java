package com.nfinity.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class PortfolioVO {
    private long coinId;
    private String symbol;
    private String icon;
    private BigDecimal useAmount;
    private String address;
}
