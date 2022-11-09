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
  private int status;
  private String banner;
  private String title;
  private String description;
  private int unlockablePoints;
  private int unlockablePerClaimPoints;
  private int unlockablePerVideoWatchPoints;
  private int youtubeVideo;
  private int youtubePerVideoWatchPoints;
  private int youtubePerEventCheckPoints;
  private int maxUnlockableClaimPerMonthPoints;
  private int maxVideoWatchPerMonthPoints;
  private int maxYoutubeVideoPerMonthPoints;
  private int max_event_check_per_month_points;
  private int tiersCreation;
  private int totalTier;
  private int pointsRedeemRequiredForTierUpgrade;
  private int redeemTicketPoints;
  private int redeemCouponPoints;
  private int pointsExpiration;
  private int expirationMonths;
  private int step;
  private Timestamp createTime;
  private Timestamp updateTime;
}
