package com.nfinity.repository;

import com.nfinity.entity.OrderNftEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderNftRepository extends JpaRepository<OrderNftEntity, Long> {
    int countByUserId(Long userId);

    List<OrderNftEntity> findAllByUserId(Long userId, Pageable pageable);
}
