package com.nfinity.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "collection")
public class CollectionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String icon;
    private int category;
    private String domainName;
    private String contractChain;
    private String symbol;
    private int totalSupply;
    private BigDecimal mintPrice;
    private String description;
    private int airdropRetention;
    private int retainedQty;
    private String address;
    private BigDecimal revenue;
    private int mintedQty;
    private int status;
    private int contractStatus;
}
