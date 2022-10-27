package com.nfinity.repository;

import com.nfinity.entity.CeFinanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CeFinanceRepository extends JpaRepository<CeFinanceEntity, Long> {
    List<CeFinanceEntity> findAllByUserId(Long userId);
}
