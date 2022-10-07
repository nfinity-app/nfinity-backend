package com.nfinity.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "draft_collection_folder_nft")
public class DraftCollectionFolderNftEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long collectionId;
    private Long folderId;
    private Long nftId;
    private int nftStatus;
    private Timestamp createTime;
    private Timestamp updateTime;
}
