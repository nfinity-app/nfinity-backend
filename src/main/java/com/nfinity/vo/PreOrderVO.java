package com.nfinity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class PreOrderVO {
    private Long id;

    @NotNull
    @JsonProperty("collection_id")
    private Long collectionId;

    @NotNull
    @JsonProperty("user_id")
    private Long userId;

    @Min(1)
    @NotNull
    @JsonProperty("mint_qty")
    private Integer mintQty;
}
