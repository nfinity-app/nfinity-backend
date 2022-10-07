package com.nfinity.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "chain_nft_contract")
public class ChainNftContractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Long collectionId;
    private String chainType;
    private int contractType;
    private String contractAddr;
    private String owner;
    private long totalNum;
    private long mintNum;
    private long airdropNum;
    private BigDecimal profit;
    private BigDecimal price;
    private int status;
    private int airdropStatus;
    private String name;
    private String symbol;
    private String txId;
    private long blockTime;
    private long blockNum;
    private BigDecimal fee;
    private Timestamp createTime;
    private Timestamp updateTime;
}
