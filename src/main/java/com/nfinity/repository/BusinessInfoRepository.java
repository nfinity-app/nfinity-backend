package com.nfinity.repository;

import com.nfinity.entity.BusinessInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessInfoRepository extends JpaRepository<BusinessInfoEntity, Long> {
}
