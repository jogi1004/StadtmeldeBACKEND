package com.stadtmeldeapp.Repository;


import com.stadtmeldeapp.Entity.StatusEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StatusRepository extends JpaRepository<StatusEntity, Integer>{
List<StatusEntity> findByReportingLocationEntityId(int reportingLocationId);
    
}
