package com.nfinity.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Setter
@Getter
@Entity
@Table(name = "chain_bill")
public class ChainBillEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
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
    private String chainType;
    private String txId;
    private BigDecimal qty;
    private int status;
    private Long blockNum;
    private Long blockTime;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String comment;
}
