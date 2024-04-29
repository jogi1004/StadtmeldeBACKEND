package com.stadtmeldeapp.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stadtmeldeapp.Entity.BasicReportEntity;

@Repository
public interface BasicReportRepository extends JpaRepository<BasicReportEntity, Integer> {

    List<BasicReportEntity> findAllByReportingLocationId(int reportingLocationId);

    List<BasicReportEntity> findAllByReportingLocationName(String reportingLocationName);

    List<BasicReportEntity> findFirst10ByReportingLocationIdOrderByReportingTimestampDesc(Integer reportingLocationId);
}
