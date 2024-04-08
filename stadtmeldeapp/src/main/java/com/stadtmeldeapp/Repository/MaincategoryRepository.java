package com.stadtmeldeapp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stadtmeldeapp.Entity.MaincategoryEntity;

import java.util.List;


public interface MaincategoryRepository extends JpaRepository<MaincategoryEntity, Integer> {
    List<MaincategoryEntity> findByReportingLocationEntity_Name(String name);
}
