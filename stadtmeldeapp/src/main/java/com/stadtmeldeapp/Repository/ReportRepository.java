package com.stadtmeldeapp.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.stadtmeldeapp.Entity.ReportEntity;

public interface ReportRepository extends JpaRepository<ReportEntity, Integer> {

    List<ReportEntity> findAllByUserId(int userId);

    List<ReportEntity> findAllByReportingLocationId(int reportingLocationId);

    List<ReportEntity> findAllByReportingLocationName(String reportingLocationName);

    List<ReportEntity> findFirst10ByReportingLocationIdOrderByReportingTimestampDesc(Integer reportingLocationId);
}
