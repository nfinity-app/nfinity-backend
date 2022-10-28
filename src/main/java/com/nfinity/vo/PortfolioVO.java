package com.nfinity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class PortfolioVO {
    @JsonProperty("coin_id")
    private long coinId;
    private String symbol;
    private String img;
    @JsonProperty("amount")
    private BigDecimal useAmount;
}
