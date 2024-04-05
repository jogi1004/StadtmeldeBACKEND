package com.stadtmeldeapp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stadtmeldeapp.Entity.SubcategoryEntity;

public interface SubcategoryRepository extends JpaRepository<SubcategoryEntity, Integer> {

    //List<SubcategoryEntity> findByMaincategoryId(int maincategoryId);
}
