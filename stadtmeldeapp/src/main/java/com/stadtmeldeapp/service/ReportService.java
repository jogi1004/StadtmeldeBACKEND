package com.stadtmeldeapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stadtmeldeapp.CustomExceptions.NotAllowedException;
import com.stadtmeldeapp.CustomExceptions.NotFoundException;
import com.stadtmeldeapp.DTO.ReportDTO;
import com.stadtmeldeapp.DTO.ReportDetailInfoDTO;
import com.stadtmeldeapp.DTO.ReportInfoDTO;
import com.stadtmeldeapp.DTO.ReportUpdateDTO;
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

import java.io.IOException;
import java.util.ArrayList;
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

    public ReportEntity createReport(ReportDTO reportDto, String username)
            throws NotFoundException, IOException {
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
        List<StatusEntity> status = statusRepository.findByReportingLocationEntityId(reportingLocation.getId());

        StatusEntity statusX = null;
        if (!status.isEmpty()) {
            statusX = status.get(0);
        }

        ReportEntity report = new ReportEntity();
        report.setSubcategory(subcategory);
        if (reportDto.title() != null)
            report.setTitle(reportDto.title());
        report.setDescription(reportDto.description());
        report.setLongitude(reportDto.longitude());
        report.setLatitude(reportDto.latitude());
        report.setReportingLocation(reportingLocation);
        report.setUser(user);
        report.setStatus(statusX);
        if (reportDto.additionalPicture() != null)
            report.setAdditionalPicture(reportDto.additionalPicture());

        return reportRepository.save(report);
    }

    public List<ReportInfoDTO> getReportsByUserId(int userId) {
        return toInfoDTOList(reportRepository.findAllByUserId(userId));

    }

    public List<ReportInfoDTO> getReportsByUserId(HttpServletRequest request) throws NotFoundException {
        int userId = userService.getUserFromRequest(request).getId();
        return getReportsByUserId(userId);
    }

    public List<ReportInfoDTO> getReportsByReportingLocationId(int reportingLocationId) {
        return toInfoDTOList(reportRepository.findAllByReportingLocationId(reportingLocationId));
    }

    public List<ReportEntity> getReportEntitiesByReportingLocationId(int reportingLocationId) {
        return reportRepository.findAllByReportingLocationId(reportingLocationId);
    }

    public List<ReportInfoDTO> getReportsByReportingLocationName(String reportingLocationTitle) {
        return toInfoDTOList(reportRepository.findAllByReportingLocationName(reportingLocationTitle));
    }

    public ReportDetailInfoDTO getReportDetails(int id) throws NotFoundException {
        ReportEntity report = reportRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Meldung nicht gefunden"));
        return new ReportDetailInfoDTO(
                (report.getTitle() == null || report.getTitle().isBlank()) ? report.getSubcategory().getTitle()
                        : report.getTitle(),
                report.getDescription(),
                -1, report.getStatus(), report.getReportingTimestamp(), report.getAdditionalPicture(),
                report.getLongitude(),
                report.getLatitude(), report.getUser().getUsername(), report.getReportingLocation().getName(),
                report.getUser().getProfilePicture());
    }
    
    public List<ReportInfoDTO> getLatestReportsByReportingLocationId(int id) {
        return toInfoDTOList(reportRepository.findFirst10ByReportingLocationIdOrderByReportingTimestampDesc(id));
    }

    public ReportInfoDTO updateReport(int reportId, ReportUpdateDTO reportDto,
            HttpServletRequest request) throws NotAllowedException, NotFoundException {

        ReportEntity report = reportRepository.findById(reportId)
                .orElseThrow(() -> new NotFoundException("Meldung nicht gefunden"));

        if (!report.getUser().equals(userService.getUserFromRequest(request))) {
            throw new NotAllowedException("Keine Berechtigung!");
        }
        if (reportDto.title() != null && !report.getSubcategory().getTitle().equals("Sonstiges")) {
            throw new NotAllowedException("Meldungen außerhalb der Kategorie 'Sonstiges' haben keinen Titel");
        }
        report.setTitle(reportDto.title());
        report.setDescription(reportDto.description());
        report.setAdditionalPicture(reportDto.additionalPicture());
        return toReportInfoDTO(reportRepository.save(report));
    }

    public ReportInfoDTO updateReportStatus(int reportId, String newStatus) throws NotFoundException {

        ReportEntity report = reportRepository.findById(reportId)
                .orElseThrow(() -> new NotFoundException("Meldung nicht gefunden"));

        StatusEntity status = statusRepository
                .findByReportingLocationEntityIdAndName(report.getReportingLocation().getId(), newStatus)
                .orElseThrow(() -> new NotFoundException("Status nicht gefunden"));
        report.setStatus(status);
        return toReportInfoDTO(report);
    }

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

    public List<ReportInfoDTO> toInfoDTOList(List<ReportEntity> reports) {
        List<ReportInfoDTO> retReports = new ArrayList<>();

        for (ReportEntity r : reports) {
            retReports
                    .add(new ReportInfoDTO(
                            (r.getTitle() == null || r.getTitle().isBlank()) ? r.getSubcategory().getTitle()
                                    : r.getTitle(), (r.getSubcategory().getMaincategoryEntity().getIconEntity() == null ? -1 : r.getSubcategory().getMaincategoryEntity().getIconEntity().getId()), r.getStatus(), r.getReportingTimestamp(), r.getAdditionalPicture(),
                            r.getLongitude(),
                            r.getLatitude()));
        }
        return retReports;
    }

    public ReportInfoDTO toReportInfoDTO(ReportEntity report) {
        return new ReportInfoDTO(
                (report.getTitle() == null || report.getTitle().isBlank()) ? report.getSubcategory().getTitle()
                        : report.getTitle(),
                        (report.getSubcategory().getMaincategoryEntity().getIconEntity() == null ? -1 : report.getSubcategory().getMaincategoryEntity().getIconEntity().getId()), report.getStatus(), report.getReportingTimestamp(), report.getAdditionalPicture(),
                report.getLongitude(),
                report.getLatitude());
    }

    public ReportInfoDTO toReportInfoDTO(ReportDetailInfoDTO report) {
        return new ReportInfoDTO(
                report.titleOrsubcategoryName(),
                report.iconId(), report.status(), report.timestamp(), report.image(),
                report.longitude(),
                report.latitude());
    }
}
