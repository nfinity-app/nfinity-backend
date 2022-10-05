package com.nfinity.repository;

import com.nfinity.entity.DraftCollectionFolderNftEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DraftCollectionFolderNftRepository extends JpaRepository<DraftCollectionFolderNftEntity, Long> {
}
