package com.nfinity.repository;

import com.nfinity.entity.ChainNftContractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChainNftContractRepository extends JpaRepository<ChainNftContractEntity, Long> {
    ChainNftContractEntity findByCollectionId(Long collectionId);
}
