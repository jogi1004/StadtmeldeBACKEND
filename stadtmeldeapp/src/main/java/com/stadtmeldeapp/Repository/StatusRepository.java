package com.stadtmeldeapp.Repository;

import com.stadtmeldeapp.Entity.StatusEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<StatusEntity, Integer> {
    List<StatusEntity> findByReportingLocationEntityId(int reportingLocationId);
    Optional<StatusEntity> findByReportingLocationEntityIdAndName(int reportingLocationId, String statusName);
}
