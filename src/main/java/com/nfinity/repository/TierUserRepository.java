package com.nfinity.repository;

import com.nfinity.entity.TierUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TierUserRepository extends JpaRepository<TierUserEntity, Long> {
    long countByTierId(Long tierId);
}
