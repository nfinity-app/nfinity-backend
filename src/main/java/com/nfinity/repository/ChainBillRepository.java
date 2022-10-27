package com.nfinity.repository;

import com.nfinity.entity.ChainBillEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChainBillRepository extends JpaRepository<ChainBillEntity, Long> {
    List<ChainBillEntity> findAllByUserId(Long userId, Pageable pageable);

    int countByUserId(Long userId);
}
