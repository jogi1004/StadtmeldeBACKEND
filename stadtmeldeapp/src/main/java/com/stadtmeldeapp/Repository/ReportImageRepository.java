package com.stadtmeldeapp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stadtmeldeapp.Entity.ReportImageEntity;

@Repository
public interface ReportImageRepository extends JpaRepository<ReportImageEntity, Integer> {
}