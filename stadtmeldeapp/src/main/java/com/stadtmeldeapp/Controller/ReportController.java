package com.stadtmeldeapp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.stadtmeldeapp.CustomExceptions.NotAllowedException;
import com.stadtmeldeapp.CustomExceptions.NotFoundException;
import com.stadtmeldeapp.DTO.ReportDTO;
import com.stadtmeldeapp.DTO.ReportDetailInfoDTO;
import com.stadtmeldeapp.DTO.ReportUpdateDTO;
import com.stadtmeldeapp.Entity.ReportEntity;
import com.stadtmeldeapp.service.EmailSenderService;
import com.stadtmeldeapp.service.ReportService;
import com.stadtmeldeapp.service.StaticMapService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private StaticMapService staticMapService;

    @PostMapping
    public ResponseEntity<ReportEntity> createReport(
            @RequestBody ReportDTO reportDTO) throws NotFoundException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        ReportEntity createdReport = reportService.createReport(reportDTO, username);
        return new ResponseEntity<>(createdReport, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReportDetailInfoDTO>> getAllReportsByUserId(@PathVariable int userId) {
        List<ReportDetailInfoDTO> reports = reportService.getReportsByUserId(userId);
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ReportDetailInfoDTO>> getMyReports(HttpServletRequest request) throws NotFoundException {
        List<ReportDetailInfoDTO> reports = reportService.getReportsByUserId(request);
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }

    @GetMapping("/location/id/{locationId}")
    public ResponseEntity<List<ReportDetailInfoDTO>> getReportsByLocationId(@PathVariable int locationId) {
        List<ReportDetailInfoDTO> reports = reportService.getReportsByReportingLocationId(locationId);
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }

    @GetMapping("/location/name/{locationName}")
    public ResponseEntity<List<ReportDetailInfoDTO>> getReportsByLocationName(@PathVariable String locationName) {
        List<ReportDetailInfoDTO> reports = reportService.getReportsByReportingLocationName(locationName);
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportDetailInfoDTO> getReportDetails(@PathVariable int id) throws NotFoundException {
        ReportDetailInfoDTO report = reportService.getReportDetails(id);
        return new ResponseEntity<>(report, HttpStatus.OK);
    }

    @PutMapping("/{reportId}")
    public ResponseEntity<ReportDetailInfoDTO> updateReport(@PathVariable int reportId,
            @RequestBody ReportUpdateDTO reportDto,
            HttpServletRequest request) throws NotAllowedException, NotFoundException {
        ReportDetailInfoDTO updatedReport = reportService.updateReport(reportId, reportDto,
                request);

        return new ResponseEntity<>(updatedReport, HttpStatus.OK);
    }

    @PutMapping("/admin/{reportId}")
    public ResponseEntity<ReportDetailInfoDTO> updateReportStatus(@PathVariable int reportId, @RequestBody String newStatus) throws Exception {
        ReportDetailInfoDTO updatedReport = reportService.updateReportStatus(reportId, newStatus);
        ReportEntity reportEntity = reportService.getReportById(reportId);
        if (reportEntity != null)
            try {
                emailSenderService.sendStatusChangeEmail(reportEntity.getUser().getEmail(),
                        reportEntity.getUser().getUsername(),
                        (reportEntity.getTitle() == null || reportEntity.getTitle().isBlank())
                                ? reportEntity.getSubcategory().getTitle()
                                : reportEntity.getTitle(),
                        reportEntity.getStatus().getName(),
                        new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY).format(reportEntity.getReportingTimestamp()),
                        staticMapService.getMapImage(reportEntity.getLatitude(), reportEntity.getLongitude()));
            } catch (MessagingException | IOException e) {
                e.printStackTrace();
            }
        return new ResponseEntity<>(updatedReport, HttpStatus.OK);
    }

    @DeleteMapping("/{reportId}")
    public ResponseEntity<Void> deleteReport(@PathVariable int reportId, HttpServletRequest request)
            throws NotFoundException, NotAllowedException {
        reportService.deleteReport(reportId, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}