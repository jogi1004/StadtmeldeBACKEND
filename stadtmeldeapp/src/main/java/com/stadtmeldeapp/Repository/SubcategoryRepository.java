package com.stadtmeldeapp.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stadtmeldeapp.Entity.SubcategoryEntity;

public interface SubcategoryRepository extends JpaRepository<SubcategoryEntity, Integer> {
    List<SubcategoryEntity> findByMaincategoryEntity_Id(int mainCategoryId);
    Optional<SubcategoryEntity> findByTitleAndMaincategoryEntityId(String name, int maincategoryId);
}
