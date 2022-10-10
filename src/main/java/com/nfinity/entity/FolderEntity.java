package com.nfinity.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "folder")
public class FolderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String icon;
    private int mintStatus;
    private Timestamp createTime;
    private Timestamp updateTime;
}
