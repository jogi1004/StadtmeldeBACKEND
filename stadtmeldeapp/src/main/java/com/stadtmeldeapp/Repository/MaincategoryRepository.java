package com.stadtmeldeapp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stadtmeldeapp.Entity.MaincategoryEntity;

import java.util.List;
import java.util.Optional;


public interface MaincategoryRepository extends JpaRepository<MaincategoryEntity, Integer> {
    List<MaincategoryEntity> findByReportingLocationEntity_Name(String name);

    Optional<MaincategoryEntity> findByTitleAndReportingLocationEntityId(String mainCategoryName, int id);
}
