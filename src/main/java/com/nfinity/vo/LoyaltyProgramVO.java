package com.nfinity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class LoyaltyProgramVO {

  private Long id;

  @JsonProperty("user_id")
  private Long userId;

  @NotBlank
  private String banner;
  @NotBlank
  private String title;
  @NotBlank
  private String description;

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


  @NotNull
  @JsonProperty("unlockable_points")
  private Integer unlockablePoints;

  @JsonProperty("unlockable_per_claim_points")
  private Integer unlockablePerClaimPoints;

  @JsonProperty("unlockable_per_video_watch_points")
  private Integer unlockablePerVideoWatchPoints;

  @NotNull
  @JsonProperty("youtube_video")
  private Integer youtubeVideo;

  @JsonProperty("youtube_per_video_watch_points")
  private Integer youtubePerVideoWatchPoints;

  @JsonProperty("youtube_per_event_check_points")
  private Integer youtubePerEventCheckPoints;

  @NotNull
  @JsonProperty("max_unlockable_claim_per_month_points")
  private Integer maxUnlockableClaimPerMonthPoints;

  @NotNull
  @JsonProperty("max_video_watch_per_month_points")
  private Integer maxVideoWatchPerMonthPoints;

  @NotNull
  @JsonProperty("max_youtube_video_per_month_points")
  private Integer maxYoutubeVideoPerMonthPoints;

  @NotNull
  @JsonProperty("max_event_check_per_month_points")
  private Integer max_event_check_per_month_points;

  @NotNull
  @JsonProperty("tiers_creation")
  private Integer tiersCreation;

  @Max(5)
  @JsonProperty("total_tier")
  private Integer totalTier;

  @NotNull
  @JsonProperty("points_redeem_required_for_tier_upgrade")
  private Integer pointsRedeemRequiredForTierUpgrade;

  private List<TierVO> tiers;

  @NotNull
  @JsonProperty("redeem_ticket_points")
  private Integer redeemTicketPoints;

  @NotNull
  @JsonProperty("redeem_coupon_points")
  private Integer redeemCouponPoints;

  @NotNull
  @JsonProperty("points_expiration")
  private Integer pointsExpiration;

  @JsonProperty("expirationMonths")
  private Integer expirationMonths;

  private Integer step;
}
