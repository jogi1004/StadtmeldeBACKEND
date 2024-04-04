package com.stadtmeldeapp.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stadtmeldeapp.Entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer>{
    Optional<UserEntity> findByUsername(String username);
    boolean existsByUsername(String username);
}