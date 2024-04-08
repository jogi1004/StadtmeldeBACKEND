package com.stadtmeldeapp.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stadtmeldeapp.Entity.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
Optional<RoleEntity> findByName(String name);
}