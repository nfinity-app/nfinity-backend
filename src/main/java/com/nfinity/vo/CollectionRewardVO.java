package com.nfinity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class CollectionRewardVO {
    @NotNull
    @JsonProperty("collection_id")
    private Long collectionId;

    @NotBlank
    @JsonProperty("collection_name")
    private String collectionName;

    @NotNull
    @JsonProperty("nft_rewards")
    private Integer nftRewards;

    @JsonProperty("nft_rewards_points")
    private Integer nftRewardsPoints;

    @NotNull
    @JsonProperty("allow_repetitive_counting")
    private Integer allowRepetitiveCounting;

    @JsonProperty("max_repetitive_counting")
    private Integer maxRepetitiveCounting;

    @NotNull
    @JsonProperty("twitter_engagement")
    private Integer twitterEngagement;

    @JsonProperty("twitter_photo")
    private String twitterPhoto;

    @JsonProperty("twitter_user_id")
    private String twitterUserId;

    @JsonProperty("twitter_username")
    private String twitterUsername;

    @JsonProperty("twitter_follow_points")
    private Integer twitterFollowPoints;

    @JsonProperty("twitter_per_post_like_points")
    private Integer twitterPerPostLikePoints;

    @NotNull
    @JsonProperty("instagram_engagement")
    private Integer instagramEngagement;

    @JsonProperty("instagram_photo")
    private String instagramPhoto;

    @JsonProperty("instagram_user_id")
    private String instagramUserId;

    @JsonProperty("instagram_username")
    private String instagramUsername;

    @JsonProperty("instagram_follow_points")
    private Integer instagramFollowPoints;

    @JsonProperty("instagram_hashtags")
    private List<InstagramHashtagVO> instagramHashtags;
}
