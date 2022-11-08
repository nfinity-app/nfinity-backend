package com.nfinity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TwitterVO {

    private List<TwitterUserVO> data;

    @Getter
    @Setter
    public static class TwitterUserVO {
        private String id;
        private String username;
        private String name;
        @JsonProperty("profile_image_url")
        private String profileImageUrl;
    }
}
