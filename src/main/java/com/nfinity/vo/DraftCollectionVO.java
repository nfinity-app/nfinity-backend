package com.nfinity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class DraftCollectionVO {
    private Long id;

    @JsonProperty("collection_name")
    private String collectionName;

    private Integer category;

    @JsonProperty("domain_name")
    private String domainName;

    @JsonProperty("contract_chain")
    private String contractChain;

    private String symbol;

    @JsonProperty("total_supply")
    private Integer totalSupply;

    @JsonProperty("mint_price")
    private BigDecimal mintPrice;

    private String description;

    @JsonProperty("airdrop_retention")
    private Integer airdropRetention;

    @JsonProperty("retained_qty")
    private Integer retainedQty;

    @JsonProperty("folder_id")
    private Long folderId;

    @JsonProperty("folder_name")
    private String folderName;

    private List<NftVO> records;
}
