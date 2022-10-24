package com.nfinity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@Setter
public class BusinessInfoVO {
    private Long id;

    @NotNull
    private Integer type;

    @NotBlank
    private String name;

    @NotNull
    private Integer category;

    @NotNull
    @JsonProperty("birth_date")
    private Timestamp birthDate;

    private String logo;

    private String bio;

    private Long userId;
}