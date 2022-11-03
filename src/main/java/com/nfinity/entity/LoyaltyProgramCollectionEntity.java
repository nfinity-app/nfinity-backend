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
  private long nftRewards;
  private long nftRewardsPoints;
  private long allowRepetitiveCounting;
  private long maxRepetitiveCounting;
  private int twitterEngagement;
  private String twitterPhoto;
  private String twitterUsername;
  private long twitterFollowPoints;
  private long twitterPerPostLikePoints;
  private int instagramEngagement;
  private String instagramPhoto;
  private String instagramUsername;
  private long instagramFollowPoints;
  private Timestamp createTime;
  private Timestamp updateTime;

}
