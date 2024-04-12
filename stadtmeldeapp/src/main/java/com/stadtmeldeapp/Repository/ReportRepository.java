package com.stadtmeldeapp.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stadtmeldeapp.Entity.ReportEntity;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Integer> {

    List<ReportEntity> findAllByUserId(int userId);

    List<ReportEntity> findAllByReportingLocationId(int reportingLocationId);

    List<ReportEntity> findAllByReportingLocationName(String reportingLocationName);
}
