package com.nfinity.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "loyalty_program")
public class LoyaltyProgramEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private long userId;
  private String banner;
  private String title;
  private String description;
  private int unlockablePoints;
  private long unlockablePerClaimPoints;
  private long unlockablePerVideoWatchPoints;
  private int youtubeVideo;
  private long youtubePerVideoWatchPoints;
  private long youtubePerEventCheckPoints;
  private long maxUnlockableClaimPerMonthPoints;
  private long maxVideoWatchPerMonthPoints;
  private long maxYoutubeVideoPerMonthPoints;
  private long max_event_check_per_month_points;
  private int tiersCreation;
  private long totalTier;
  private int pointsRedeemRequiredForTierUpgrade;
  private long redeemTicketPoints;
  private long redeemCouponPoints;
  private int pointsExpiration;
  private long expirationMonths;
  private Timestamp createTime;
  private Timestamp updateTime;
}
