package com.nfinity.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "draft_collection")
public class DraftCollectionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String icon;
    private int category;
    @JsonProperty("domain_name")
    private String domainName;
    @JsonProperty("contract_chain")
    private String contractChain;
    private String symbol;
    @JsonProperty("total_supply")
    private int totalSupply;
    @JsonProperty("mint_price")
    private BigDecimal mintPrice;
    private String description;
    @JsonProperty("airdrop_retention")
    private int airdropRetention;
    @JsonProperty("retained_qty")
    private int retainedQty;
    private int status;
}
