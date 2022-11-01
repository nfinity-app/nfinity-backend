package com.nfinity.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
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

    private String birthDate;

    private String logo;

    private String bio;

    private Long userId;

    private String website;

    private String email;

    private String phoneNumber;

    private String address;

    private BigDecimal lat;

    private BigDecimal lng;

    private String facebook;

    private String twitter;

    private String instagram;

    private String youtube;

    private String medium;

    private Timestamp createTime;

    private Timestamp updateTime;
}
