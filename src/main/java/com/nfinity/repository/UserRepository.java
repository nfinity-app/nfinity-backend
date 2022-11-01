package com.nfinity.repository;

import com.nfinity.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query(value = "select * from user where status = ?1 and (email = ?2 or username = ?3)", nativeQuery = true)
    UserEntity findByEmailOrUsernameAndStatus(int status, String email, String username);

    UserEntity findByEmailAndStatus(String email, int status);

    UserEntity findByUsernameAndStatus(String username, int status);
}
