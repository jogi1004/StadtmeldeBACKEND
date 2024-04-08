package com.stadtmeldeapp.Repository;


import com.stadtmeldeapp.Entity.StatusEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<StatusEntity, Integer>{

    
}
