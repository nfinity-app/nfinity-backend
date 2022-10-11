package com.nfinity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CollectionInputVO {
    @NotBlank
    @JsonProperty("collection_name")
    private String collectionName;

    @NotNull
    private Integer category;

    @NotBlank
    @JsonProperty("domain_name")
    private String domainName;

    @NotBlank
    @JsonProperty("contract_chain")
    private String contractChain;

    @NotBlank
    private String symbol;

    @NotNull
    @JsonProperty("total_supply")
    private Integer totalSupply;

    @JsonProperty("mint_price")
    private BigDecimal mintPrice;

    @NotBlank
    private String description;

    @NotNull
    @JsonProperty("airdrop_retention")
    private Integer airdropRetention;

    @JsonProperty("retained_qty")
    private Integer retainedQty;

    @NotNull
    @JsonProperty("folder_id")
    private Long folderId;

    @NotEmpty
    @Valid
    private List<@NotNull NftVO> records;
}
