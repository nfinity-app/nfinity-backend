package com.nfinity.repository;

import com.nfinity.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);

    UserEntity findByEmailOrUsername(String email, String username);

    UserEntity findByEmailAndStatus(String email, int status);
}
