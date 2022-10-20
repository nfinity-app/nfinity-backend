package com.nfinity.repository;

import com.nfinity.entity.OrderNftEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderNftRepository extends JpaRepository<OrderNftEntity, Long> {
    @Query(value = "select b.* from `order` a, order_nft b where a.user_id = ?1 and a.status = 2 and a.id = b.order_id", nativeQuery = true)
    List<OrderNftEntity> findAllByUserIdAndStatus(Long userId, Pageable pageable);
}
