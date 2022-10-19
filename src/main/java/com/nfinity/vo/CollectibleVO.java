package com.nfinity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CollectibleVO {
    @JsonProperty("collection_id")
    private Long collectionId;

    @JsonProperty("collection_name")
    private String collectionName;

    @JsonProperty("nft_id")
    private Long nftId;

    @JsonProperty("nft_path")
    private String nftPath;

    @JsonProperty("token_id")
    private Long tokenId;
}
