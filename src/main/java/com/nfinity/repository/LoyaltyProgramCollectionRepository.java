package com.nfinity.repository;

import com.nfinity.entity.LoyaltyProgramCollectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoyaltyProgramCollectionRepository extends JpaRepository<LoyaltyProgramCollectionEntity, Long> {
    Optional<LoyaltyProgramCollectionEntity> findFirstByProgramId(Long programId);
}
