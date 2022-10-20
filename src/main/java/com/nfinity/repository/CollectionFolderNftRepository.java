package com.nfinity.repository;

import com.nfinity.entity.CollectionFolderNftEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectionFolderNftRepository extends JpaRepository<CollectionFolderNftEntity, Long> {
    List<CollectionFolderNftEntity> findAllByCollectionId(Long collectionId);

    List<CollectionFolderNftEntity> findAllByCollectionIdAndNftStatus(Long collectionId, int nftStatus);

    @Query(value = "select count(*) from collection_folder_nft a, nft b where a.collection_id = ?1 and a.nft_id = b.id and b.mint_status = 2", nativeQuery = true)
    int countAllByCollectionIdAndMintStatus(Long collectionId);
    CollectionFolderNftEntity findByCollectionIdAndNftId(Long collectionId, Long id);
}
