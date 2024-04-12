package com.stadtmeldeapp.Repository;


import com.stadtmeldeapp.Entity.StatusEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusRepository extends JpaRepository<StatusEntity, Integer>{
List<StatusEntity> findByReportingLocationEntityId(int reportingLocationId);
    
}
