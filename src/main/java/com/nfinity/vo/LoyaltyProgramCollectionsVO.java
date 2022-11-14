package com.nfinity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class LoyaltyProgramCollectionsVO {

  private Long id;

  @JsonProperty("user_id")
  private Long userId;

  private Integer status;

  @NotBlank
  private String banner;
  @NotBlank
  private String title;
  @NotBlank
  private String description;

  @NotEmpty
  @JsonProperty("collection_rewards")
  private List<@Valid CollectionRewardVO> collectionRewards;

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

  @JsonProperty("expiration_months")
  private Integer expirationMonths;

  private Integer step;
}
