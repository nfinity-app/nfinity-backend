package com.nfinity.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Setter
@Getter
@Entity
@Table(name = "chain_address")
public class ChainAddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private long coinId;
    private long userId;
    private String chainType;
    private String address;
    private String vaultId;
    private String name;
    private String assetId;
    private BigDecimal chainAmount;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String comment;
}
