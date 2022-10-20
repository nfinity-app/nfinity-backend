package com.nfinity.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Setter
@Getter
@Entity
@Table(name = "`order`")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long collectionId;
    private Long userId;
    private String paymentId;
    private int mintQty;
    private BigDecimal amount;
    private int status;
    private Timestamp createTime;
    private Timestamp updateTime;
}
