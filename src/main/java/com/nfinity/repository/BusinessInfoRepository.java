package com.nfinity.repository;

import com.nfinity.entity.BusinessInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessInfoRepository extends JpaRepository<BusinessInfoEntity, Long> {
    Optional<BusinessInfoEntity> findByIdAndUserId(Long businessId, Long userId);

    boolean existsByUserId(Long userId);
}
