package com.nfinity.repository;

import com.nfinity.entity.DraftCollectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DraftCollectionRepository extends JpaRepository<DraftCollectionEntity, Long> {
}
