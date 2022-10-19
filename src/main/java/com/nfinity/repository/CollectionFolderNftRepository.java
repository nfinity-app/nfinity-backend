package com.nfinity.repository;

import com.nfinity.entity.CollectionFolderNftEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectionFolderNftRepository extends JpaRepository<CollectionFolderNftEntity, Long> {
    List<CollectionFolderNftEntity> findAllByCollectionId(Long collectionId);

    List<CollectionFolderNftEntity> findAllByCollectionIdAndNftStatus(Long collectionId, int nftStatus);
}
