package com.stadtmeldeapp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stadtmeldeapp.Entity.ReportPictureEntity;

@Repository
public interface ReportPictureRepository extends JpaRepository<ReportPictureEntity, Integer> {
    
}
