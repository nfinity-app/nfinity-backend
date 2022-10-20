package com.nfinity.repository;

import com.nfinity.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    @Query(value = "select count(*) from `order` a, order_nft b where a.user_id = ?1 and a.status = 2 and a.id = b.order_id;", nativeQuery = true)
    int countByUserIdAndStatus(Long userId);
}
