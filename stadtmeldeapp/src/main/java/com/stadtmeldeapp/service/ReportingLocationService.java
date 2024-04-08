package com.stadtmeldeapp.service;

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

    public ReportingLocationEntity getReportingLocationByName(String name){
        return reportingLocationRepository.findReportingLocationByName(name).orElse(null);
    }

    public ReportingLocationEntity getReportingLocationById(int id){
        return reportingLocationRepository.findById(id).orElse(null);
    }

    public ReportingLocationEntity createReportingLocation(ReportingLocationEntity reportingLocation){
        return reportingLocationRepository.save(reportingLocation);
    }
}
