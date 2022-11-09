package com.nfinity.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "loyalty_program_collection")
public class LoyaltyProgramCollectionEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private long programId;
  private long collectionId;
  private int nftRewards;
  private int nftRewardsPoints;
  private int allowRepetitiveCounting;
  private int maxRepetitiveCounting;
  private int twitterEngagement;
  private String twitterPhoto;
  private String twitterUserId;
  private String twitterUsername;
  private int twitterFollowPoints;
  private int twitterPerPostLikePoints;
  private int instagramEngagement;
  private String instagramPhoto;
  private String instagramUserId;
  private String instagramUsername;
  private int instagramFollowPoints;
  private Timestamp createTime;
  private Timestamp updateTime;

}
