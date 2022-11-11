package com.nfinity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialVO {
    @JsonProperty("collection_id")
    private Long collectionId;

    @JsonProperty("collection_name")
    private String collectionName;

    @JsonProperty("total_points")
    private long totalPoints;

    private long followers;
    private long tweets;
    private long likes;
    private long retweets;
}
