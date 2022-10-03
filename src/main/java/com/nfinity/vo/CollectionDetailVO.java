package com.nfinity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CollectionDetailVO {
    @JsonProperty("mint_price")
    private BigDecimal mintPrice;

    @JsonProperty("mint_status")
    private Integer mintStatus;

    private String description;
}
