package com.stadtmeldeapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stadtmeldeapp.CustomExceptions.NotAllowedException;
import com.stadtmeldeapp.CustomExceptions.NotFoundException;
import com.stadtmeldeapp.DTO.ReportDTO;
import com.stadtmeldeapp.Entity.MaincategoryEntity;
import com.stadtmeldeapp.Entity.ReportEntity;
import com.stadtmeldeapp.Entity.ReportingLocationEntity;
import com.stadtmeldeapp.Entity.StatusEntity;
import com.stadtmeldeapp.Entity.SubcategoryEntity;
import com.stadtmeldeapp.Entity.UserEntity;
import com.stadtmeldeapp.Repository.MaincategoryRepository;
import com.stadtmeldeapp.Repository.ReportRepository;
import com.stadtmeldeapp.Repository.ReportingLocationRepository;
import com.stadtmeldeapp.Repository.StatusRepository;
import com.stadtmeldeapp.Repository.SubcategoryRepository;
import com.stadtmeldeapp.Repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@Service
@Transactional
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReportingLocationRepository reportingLocationRepository;
    @Autowired
    private SubcategoryRepository subcategoryRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private MaincategoryRepository maincategoryRepository;

    @Autowired
    private UserService userService;

    public ReportEntity createReport(ReportDTO reportDto, String username) throws NotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Nutzer nicht gefunden"));
        ReportingLocationEntity reportingLocation = reportingLocationRepository
                .findReportingLocationByName(reportDto.reportingLocationName())
                .orElseThrow(() -> new NotFoundException("Meldeort nicht gefunden"));
        MaincategoryEntity maincategory = maincategoryRepository
                .findByTitleAndReportingLocationEntityId(reportDto.mainCategoryName(), reportingLocation.getId())
                .orElseThrow(() -> new NotFoundException("Hauptkategorie nicht gefunden"));
        SubcategoryEntity subcategory = subcategoryRepository
                .findByTitleAndMaincategoryEntityId(reportDto.subCategoryName(), maincategory.getId())
                .orElseThrow(() -> new NotFoundException("Unterkategorie nicht gefunden"));
        StatusEntity status = statusRepository.findByReportingLocationEntityId(reportingLocation.getId()).get(0);

        ReportEntity report = new ReportEntity();
        report.setSubcategory(subcategory);
        report.setTitle(reportDto.title());
        report.setDescription(reportDto.description());
        report.setLongitude(reportDto.longitude());
        report.setLatitude(reportDto.latitude());
        report.setReportingLocation(reportingLocation);
        report.setUser(user);
        report.setStatus(status);

        return reportRepository.save(report);
    }

    public List<ReportEntity> getReportsByUserId(int userId) {
        return reportRepository.findAllByUserId(userId);
    }

    public List<ReportEntity> getReportsByUserId(HttpServletRequest request) throws NotFoundException {
        int userId = userService.getUserFromRequest(request).getId();
        return reportRepository.findAllByUserId(userId);
    }

    public List<ReportEntity> getReportsByReportingLocationId(int reportingLocationId) {
        return reportRepository.findAllByReportingLocationId(reportingLocationId);
    }

    public List<ReportEntity> getReportsByReportingLocationName(String reportingLocationTitle) {
        return reportRepository.findAllByReportingLocationName(reportingLocationTitle);
    }

    /*
     * public ReportEntity updateReport(int reportId, ReportDTO reportDto,
     * UserEntity user) throws Exception {
     * 
     * ReportEntity report = reportRepository.findById(reportId)
     * .orElseThrow();
     * 
     * if (!report.getUser().equals(user)) {
     * throw new Exception("You are not authorized to update this report");
     * }
     * 
     * // Update report details
     * report.setTitle(reportDto.title());
     * report.setDescription(reportDto.description());
     * report.setLongitude(reportDto.longitude());
     * report.setLatitude(reportDto.latitude());
     * return reportRepository.save(report);
     * }
     */

    public void deleteReport(int reportId, HttpServletRequest request)
            throws NotFoundException, NotAllowedException {

        ReportEntity report = reportRepository.findById(reportId)
                .orElseThrow(() -> new NotFoundException("Meldung nicht gefunden"));

        UserEntity user = userService.getUserFromRequest(request);

        if (!report.getUser().equals(user)) {
            throw new NotAllowedException("Keine Berechtigung");
        }

        reportRepository.delete(report);
    }
}