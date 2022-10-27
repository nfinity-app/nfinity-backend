package com.nfinity.repository;

import com.nfinity.entity.ChainCoinEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChainCoinRepository extends JpaRepository<ChainCoinEntity, Long> {
}
