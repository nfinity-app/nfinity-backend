package com.nfinity.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "collection_folder_nft")
public class CollectionFolderNftEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long collectionId;
    private Long folderId;
    private Long nftId;
    private Timestamp createTime;
    private Timestamp updateTime;
}
