package com.stadtmeldeapp.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stadtmeldeapp.Entity.ReportingLocationEntity;

public interface ReportingLocationRepository extends JpaRepository<ReportingLocationEntity, Integer> {
    Optional<ReportingLocationEntity> findReportingLocationByName(String name);
}
