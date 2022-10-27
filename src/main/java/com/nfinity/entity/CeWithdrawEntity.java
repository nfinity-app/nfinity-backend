package com.nfinity.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Setter
@Getter
@Entity
@Table(name = "ce_withdraw")
public class CeWithdrawEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private long coinId;
    private long userId;
    private String from;
    private String to;
    private String username;
    private BigDecimal amount;
    private BigDecimal fee;
    private BigDecimal chainAmount;
    private int status;
    private String txId;
    private String chainType;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String comment;
}
