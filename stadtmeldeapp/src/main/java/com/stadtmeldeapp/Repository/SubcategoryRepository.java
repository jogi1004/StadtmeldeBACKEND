package com.stadtmeldeapp.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stadtmeldeapp.Entity.SubcategoryEntity;

public interface SubcategoryRepository extends JpaRepository<SubcategoryEntity, Integer> {

    List<SubcategoryEntity> findByMaincategoryEntity_Id(int mainCategoryId);
}
