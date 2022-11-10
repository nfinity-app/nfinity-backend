package com.nfinity.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "tier")
public class TierEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private long programId;
  private String name;
  private int requiredPoints;
  private Timestamp createTime;
  private Timestamp updateTime;
}
