package com.nfinity.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Setter
@Getter
@Entity
@Table(name = "ce_finance")
public class CeFinanceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private long coinId;
    private long userId;
    private BigDecimal useAmount;
    private BigDecimal chainFrozen;
    private BigDecimal otcFrozen;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String comment;
}
