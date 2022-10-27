package com.nfinity.repository;

import com.nfinity.entity.ChainAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChainAddressRepository extends JpaRepository<ChainAddressEntity, Long> {
    Optional<ChainAddressEntity> findByCoinIdAndUserId(Long coinId, Long userId);

    Optional<ChainAddressEntity> findByUserIdAndChainType(Long userId, String type);
}
