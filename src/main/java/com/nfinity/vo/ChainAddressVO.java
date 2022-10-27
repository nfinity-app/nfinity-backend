package com.nfinity.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ChainAddressVO {
    private Long id;

    private long coinId;
    private long userId;
    private String chainType;
    private String address;
    private String vaultId;
    private String name;
    private String assetId;
    private BigDecimal chainAmount;
    private String comment;
}
