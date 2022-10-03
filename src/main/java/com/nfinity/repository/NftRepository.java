package com.nfinity.repository;

import com.nfinity.entity.NftEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NftRepository extends JpaRepository<NftEntity, Long> {
}
