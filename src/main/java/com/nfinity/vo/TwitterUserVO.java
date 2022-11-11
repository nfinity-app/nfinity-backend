package com.nfinity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitterUserVO {
    private String id;
    private String username;
    private String name;
    @JsonProperty("profile_image_url")
    private String profileImageUrl;
}