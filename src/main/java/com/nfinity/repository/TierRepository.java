package com.nfinity.repository;

import com.nfinity.entity.TierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TierRepository extends JpaRepository<TierEntity, Long> {
    Optional<TierEntity> findByIdAndProgramId(Long id, Long programId);

    List<TierEntity> findAllByProgramId(Long programId);
}
