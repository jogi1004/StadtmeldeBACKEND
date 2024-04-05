package com.stadtmeldeapp.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.stadtmeldeapp.Entity.ReportingLocationEntity;
import com.stadtmeldeapp.Repository.ReportingLocationRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReportingLocationService {
    private final ReportingLocationRepository reportingLocationRepository;

    public ReportingLocationService(ReportingLocationRepository reportingLocationRepository){
        this.reportingLocationRepository = reportingLocationRepository;
    }

    public Optional<ReportingLocationEntity> getReportingLocationByName(String name){
        return reportingLocationRepository.findReportingLocationByName(name);
    }

    public Optional<ReportingLocationEntity> getReportingLocationById(int id){
        return reportingLocationRepository.findById(id);
    }

    public ReportingLocationEntity createRportingLocation(ReportingLocationEntity reportingLocation){
        return reportingLocationRepository.save(reportingLocation);
    }
}
