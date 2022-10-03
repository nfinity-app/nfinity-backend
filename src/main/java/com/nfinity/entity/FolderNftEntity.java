package com.nfinity.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Table(name = "folder_nft")
@Entity
public class FolderNftEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private Long folderId;
    private Long nftId;
    @Column(name = "s3_folder_name")
    private String s3FolderName;
}
