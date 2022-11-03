package com.nfinity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("to_address")
    private String to;
    @JsonProperty("tx_type")
    private int txType;
    private String firebolcksTxId;
    private String vaultId;
    private String assetId;
    private long userId;
    private long coinId;
    private String symbol;
    private String img;
    private String chainType;
    @JsonProperty("tx_id")
    private String txId;
    @JsonProperty("amount")
    private BigDecimal qty;
    private int status;
    private long blockNum;
    private long blockTime;
    @JsonProperty("create_time")
    private Timestamp createTime;
    private String comment;
}
