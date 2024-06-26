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
import com.stadtmeldeapp.DTO.ReportInfoDTO;
import com.stadtmeldeapp.DTO.ReportUpdateDTO;
import com.stadtmeldeapp.Entity.ReportEntity;
import com.stadtmeldeapp.Entity.ReportPictureEntity;
import com.stadtmeldeapp.service.EmailSenderService;
import com.stadtmeldeapp.service.ReportPictureService;
import com.stadtmeldeapp.service.ReportService;
import com.stadtmeldeapp.service.StaticMapService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private StaticMapService staticMapService;

    @Autowired
    private ReportPictureService reportPictureService;

    @PostMapping
    public ResponseEntity<Void> createReport(
            @RequestBody ReportDTO reportDTO) throws NotFoundException, IOException, NotAllowedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        reportService.createReport(reportDTO, username);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReportInfoDTO>> getAllReportsByUserId(@PathVariable int userId) {
        List<ReportInfoDTO> reports = reportService.getReportsByUserId(userId);
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ReportInfoDTO>> getMyReports(HttpServletRequest request) throws NotFoundException {
        List<ReportInfoDTO> reports = reportService.getReportsByUserId(request);
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }

    @GetMapping("/location/id/{locationId}")
    public ResponseEntity<List<ReportInfoDTO>> getReportsByLocationId(@PathVariable int locationId) {
        List<ReportInfoDTO> reports = reportService.getReportsByReportingLocationId(locationId);
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }

    @GetMapping("/location/name/{locationName}")
    public ResponseEntity<List<ReportInfoDTO>> getReportsByLocationName(@PathVariable String locationName) {
        List<ReportInfoDTO> reports = reportService.getReportsByReportingLocationName(locationName);
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }

    @PutMapping("/{reportId}")
    public ResponseEntity<ReportInfoDTO> updateReport(@PathVariable int reportId,
            @RequestBody ReportUpdateDTO reportDto,
            HttpServletRequest request) throws NotAllowedException, NotFoundException {
        ReportInfoDTO updatedReport = reportService.updateReport(reportId, reportDto,
                request);

        return new ResponseEntity<>(updatedReport, HttpStatus.OK);
    }

    @PutMapping("/admin/{reportId}")
    public ResponseEntity<?> updateReportStatus(@PathVariable int reportId, @RequestBody String newStatus)
            throws Exception {

        ReportEntity reportEntity = reportService.getReportById(reportId);
        if (!reportEntity.getStatus().getName().equals(newStatus)) {

            ReportInfoDTO updatedReport = reportService.updateReportStatus(reportId, newStatus);
            if (reportEntity != null)
                try {
                    emailSenderService.sendStatusChangeEmail(reportEntity.getUser().getEmail(),
                            reportEntity.getUser().getUsername(),
                            (reportEntity.getTitle() == null || reportEntity.getTitle().isBlank())
                                    ? reportEntity.getSubcategory().getTitle()
                                    : reportEntity.getTitle(),
                            reportEntity.getStatus().getName(),
                            new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)
                                    .format(reportEntity.getReportingTimestamp()),
                            staticMapService.getMapImage(reportEntity.getLatitude(), reportEntity.getLongitude()));
                } catch (MessagingException | IOException e) {
                    e.printStackTrace();
                }
            return new ResponseEntity<>(updatedReport, HttpStatus.OK);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{reportId}")
    public ResponseEntity<Void> deleteReport(@PathVariable int reportId, HttpServletRequest request)
            throws NotFoundException, NotAllowedException {
        reportService.deleteReport(reportId, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/reportPicture/{reportPictureId}")
    public ResponseEntity<ReportPictureEntity> getReportPicture(@PathVariable int reportPictureId)
            throws NotFoundException {
        ReportPictureEntity reportPictureEntity = reportPictureService.getReportPictureById(reportPictureId);
        return new ResponseEntity<>(reportPictureEntity, HttpStatus.OK);
    }

}