package com.stadtmeldeapp.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stadtmeldeapp.Entity.ReportDetailsEntity;


@Repository
public interface ReportDetailsRepository extends JpaRepository<ReportDetailsEntity, Integer> {

    List<ReportDetailsEntity> findAllByUserId(int userId);
}