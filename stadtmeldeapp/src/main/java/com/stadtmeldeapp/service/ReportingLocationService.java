package com.stadtmeldeapp.service;

import java.util.Optional;
import org.springframework.stereotype.Service;

import com.stadtmeldeapp.CustomExceptions.NotFoundException;
import com.stadtmeldeapp.Entity.ReportingLocationEntity;
import com.stadtmeldeapp.Repository.ReportingLocationRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReportingLocationService {
    private final ReportingLocationRepository reportingLocationRepository;

    public ReportingLocationService(ReportingLocationRepository reportingLocationRepository) {
        this.reportingLocationRepository = reportingLocationRepository;
    }

    public boolean isReportingLocationByName(String name) {
        Optional<ReportingLocationEntity> location = reportingLocationRepository.findReportingLocationByName(name);
        if (location.isPresent())
            return true;
        return false;
    }

    public ReportingLocationEntity getReportingLocationByName(String name) throws NotFoundException {
        return reportingLocationRepository.findReportingLocationByName(name)
                .orElseThrow(() -> new NotFoundException("Meldeort nicht gefunden"));
    }

    public ReportingLocationEntity getReportingLocationById(int id) throws NotFoundException {
        return reportingLocationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Meldeort nicht gefunden"));
    }

    public ReportingLocationEntity createReportingLocation(ReportingLocationEntity reportingLocation) {

        return reportingLocationRepository.save(reportingLocation);
    }
}
