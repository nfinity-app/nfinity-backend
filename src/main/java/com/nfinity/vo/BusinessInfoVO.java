package com.nfinity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
    private String birthDate;

    private String logo;

    private String bio;

    private String website;

    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private AddressVO address;

    private String facebook;

    private String twitter;

    private String instagram;

    private String youtube;

    private String medium;

    private Long userId;
}
