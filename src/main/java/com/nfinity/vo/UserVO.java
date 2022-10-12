package com.nfinity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserVO {

    @JsonProperty("user_name")
    private String userName;
    private String email;
    @NotBlank
    private String password;
    @NotNull
    private Integer type;
}
