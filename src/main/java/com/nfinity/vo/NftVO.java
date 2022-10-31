package com.nfinity.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NftVO {
    @NotNull
    private Long id;
    @NotBlank
    private String path;
    @NotNull
    private Integer status;
    @JsonProperty("mint_status")
    private Integer mintStatus;
}
