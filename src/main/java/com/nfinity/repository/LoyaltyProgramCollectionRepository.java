package com.nfinity.repository;

import com.nfinity.entity.LoyaltyProgramCollectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoyaltyProgramCollectionRepository extends JpaRepository<LoyaltyProgramCollectionEntity, Long> {
    List<LoyaltyProgramCollectionEntity> findAllByProgramId(Long programId);

    Optional<LoyaltyProgramCollectionEntity> findByProgramIdAndCollectionId(Long id, Long collectionId);
}
