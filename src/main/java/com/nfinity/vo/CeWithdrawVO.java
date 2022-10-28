package com.nfinity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Setter
@Getter
public class CeWithdrawVO {
    private Long id;

    private long coinId;

    @NotBlank
    private String symbol;

    private long userId;
    private String from;

    @NotBlank
    @JsonProperty("to_address")
    private String to;

    private String username;

    @NotNull
    private BigDecimal amount;

    private BigDecimal fee;
    private BigDecimal chainAmount;
    private int status;
    private String txId;

    @NotBlank
    @JsonProperty("chain_type")
    private String chainType;

    private String comment;
}
