package com.stadtmeldeapp.usermanagement.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stadtmeldeapp.usermanagement.Entity.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
Optional<RoleEntity> findByName(String name);
}