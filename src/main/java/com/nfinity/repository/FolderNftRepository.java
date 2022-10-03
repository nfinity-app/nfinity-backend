package com.nfinity.repository;

import com.nfinity.entity.FolderNftEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderNftRepository extends JpaRepository<FolderNftEntity, Long> {
    FolderNftEntity findByNftId(Long nftId);

    void deleteAllByFolderId(Long folderId);
}
