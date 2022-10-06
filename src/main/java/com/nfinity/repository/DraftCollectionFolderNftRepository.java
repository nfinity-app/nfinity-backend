package com.nfinity.repository;

import com.nfinity.entity.DraftCollectionFolderNftEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DraftCollectionFolderNftRepository extends JpaRepository<DraftCollectionFolderNftEntity, Long> {
    List<DraftCollectionFolderNftEntity> findAllByCollectionId(Long collectionId);
}
