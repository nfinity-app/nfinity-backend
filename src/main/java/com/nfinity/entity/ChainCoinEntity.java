package com.nfinity.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Setter
@Getter
@Entity
@Table(name = "chain_coin")
public class ChainCoinEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
    private String symbol;
    private String code;
    private int isToken;
    private int decimals;
    private String chainType;
    private String contractAddr;
    private BigDecimal total;
    private String note;
    private int need;
    private String img;
    private Timestamp createTime;
    private Timestamp updateTime;
}
