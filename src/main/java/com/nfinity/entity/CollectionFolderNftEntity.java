package com.nfinity.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "collection_folder_nft")
public class CollectionFolderNftEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int collectionId;
    private int folderId;
    private int nftId;
}
