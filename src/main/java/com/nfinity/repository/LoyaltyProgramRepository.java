package com.nfinity.repository;

import com.nfinity.entity.LoyaltyProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoyaltyProgramRepository extends JpaRepository<LoyaltyProgramEntity, Long> {
    Optional<LoyaltyProgramEntity> findByUserId(Long userId);
}
