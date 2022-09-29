package com.nfinity.entity;

import lombok.Data;
//import javax.persistence.*;

@Data
//@Entity
//@Table(name = "folder")
public class FolderEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
//    @Column(name = "s3_name")
    private String s3Name;
    private String icon;
}
