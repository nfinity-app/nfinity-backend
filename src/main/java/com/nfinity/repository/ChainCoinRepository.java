package com.nfinity.repository;

import com.nfinity.entity.ChainCoinEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChainCoinRepository extends JpaRepository<ChainCoinEntity, Long> {
    Optional<ChainCoinEntity> findBySymbol(String symbol);
}
