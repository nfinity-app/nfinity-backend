package com.nfinity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AddressVO {
    @JsonProperty("formatted_address")
    private String address;

    private BigDecimal lat;

    private BigDecimal lng;
}
