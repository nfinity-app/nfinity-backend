package com.nfinity.repository;

import com.nfinity.entity.CollectionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionRepository extends JpaRepository<CollectionEntity, Integer> {
    @Override
    Page<CollectionEntity> findAll(Pageable pageable);
}
