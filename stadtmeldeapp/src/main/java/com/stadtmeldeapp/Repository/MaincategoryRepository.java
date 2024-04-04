package com.stadtmeldeapp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stadtmeldeapp.Entity.MaincategoryEntity;

public interface MaincategoryRepository extends JpaRepository<MaincategoryEntity, Integer> {
}
