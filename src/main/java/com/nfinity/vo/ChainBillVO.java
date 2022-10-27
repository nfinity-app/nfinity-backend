package com.nfinity.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Setter
@Getter
public class ChainBillVO {
    private Long id;

    private String reqId;
    private int billType;
    private String address;
    private String to;
    private int txType;
    private String firebolcksTxId;
    private String vaultId;
    private String assetId;
    private long userId;
    private long coinId;
    private String symbol;
    private String img;
    private String chainType;
    private String txId;
    private BigDecimal qty;
    private int status;
    private long blockNum;
    private long blockTime;
    private Timestamp createTime;
    private String comment;
}
