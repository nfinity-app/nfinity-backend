package com.nfinity.entity;

import lombok.Data;

//import javax.persistence.*;

@Data
//@Entity
//@Table(name = "nft")
public class NftEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String path;
    private int status;
}
