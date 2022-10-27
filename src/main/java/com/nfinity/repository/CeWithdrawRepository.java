package com.nfinity.repository;

import com.nfinity.entity.CeWithdrawEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CeWithdrawRepository extends JpaRepository<CeWithdrawEntity, Long> {
}
