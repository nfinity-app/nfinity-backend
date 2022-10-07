package com.nfinity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GasFeeInputVO {
    @JsonProperty("chain_type")
    private String chainType;
    @JsonProperty("tx_type")
    private int txType;
}
