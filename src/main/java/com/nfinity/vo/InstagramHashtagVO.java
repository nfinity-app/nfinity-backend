package com.nfinity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstagramHashtagVO {
    private Long id;
    private String name;
    @JsonProperty("per_like_points")
    private Integer perLikePoints;
}
