package com.nfinity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Setter
@Getter
public class OrderVO {
    private Long id;

    @NotNull
    @JsonProperty("collection_id")
    private Long collectionId;

    @NotNull
    @JsonProperty("user_id")
    private Long userId;

    @NotNull
    @JsonProperty("payment_id")
    private String paymentId;

    @NotNull
    @JsonProperty("mint_qty")
    private Integer mintQty;

    @NotNull
    private BigDecimal amount;
}
