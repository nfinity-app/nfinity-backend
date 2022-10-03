package com.nfinity.repository;

import com.nfinity.entity.FolderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FolderRepository extends JpaRepository<FolderEntity, Long> {
    @Override
    Page<FolderEntity> findAll(Pageable pageable);
}
