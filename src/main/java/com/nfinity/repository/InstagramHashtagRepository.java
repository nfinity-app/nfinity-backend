package com.nfinity.repository;

import com.nfinity.entity.InstagramHashtagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstagramHashtagRepository extends JpaRepository<InstagramHashtagEntity, Long> {
    Optional<InstagramHashtagEntity> findByIdAndUsername(Long id, String username);

    List<InstagramHashtagEntity> findAllByUsername(String instagramUsername);

    void deleteAllByProgramId(Long programId);
}
