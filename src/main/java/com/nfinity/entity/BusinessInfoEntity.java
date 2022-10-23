package com.nfinity.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "business_info")
public class BusinessInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer type;

    private String name;

    private Integer category;

    private Timestamp birthDate;

    private String logo;

    private String bio;

    private Long userId;

    private Timestamp createTime;

    private Timestamp updateTime;
}
