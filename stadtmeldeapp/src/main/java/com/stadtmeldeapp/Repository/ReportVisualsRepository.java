package com.stadtmeldeapp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stadtmeldeapp.Entity.ReportVisualsEntity;

@Repository
public interface ReportVisualsRepository extends JpaRepository<ReportVisualsEntity, Integer> {
}
