package com.nfinity.repository;

import com.nfinity.entity.CollectionFolderNftEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionFolderNftRepository extends JpaRepository<CollectionFolderNftEntity, Long> {
}
