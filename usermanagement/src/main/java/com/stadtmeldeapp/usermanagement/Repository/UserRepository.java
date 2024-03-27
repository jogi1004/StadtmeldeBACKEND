package com.stadtmeldeapp.usermanagement.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stadtmeldeapp.usermanagement.Entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long>{
    Optional<UserEntity> findByUsername(String username);
    boolean existsByUsername(String username);
}