package com.nfinity.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "instagram_hashtag")
public class InstagramHashtagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long programId;
    private String username;
    private String name;
    private int perLikePoints;
    private Timestamp createTime;
    private Timestamp updateTime;
}
