package com.nfinity.repository;

import com.nfinity.entity.FolderNftEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolderNftRepository extends JpaRepository<FolderNftEntity, Long> {
    FolderNftEntity findByNftId(Long nftId);

    void deleteAllByFolderId(Long folderId);

    List<FolderNftEntity> findAllByFolderId(Long folderId, Pageable pageable);

    int countByFolderId(Long folderId);
}
