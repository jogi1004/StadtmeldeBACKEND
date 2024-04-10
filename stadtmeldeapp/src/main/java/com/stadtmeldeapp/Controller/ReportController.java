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
import com.stadtmeldeapp.Entity.ReportEntity;
import com.stadtmeldeapp.service.ReportService;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping
    public ResponseEntity<ReportEntity> createReport(@RequestBody ReportDTO reportDto) throws NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        ReportEntity createdReport = reportService.createReport(reportDto, username);
        return new ResponseEntity<>(createdReport, HttpStatus.CREATED);
    }

    // nur für Admins verfügbar machen
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReportEntity>> getAllReportsByUserId(@PathVariable int userId) {
        List<ReportEntity> reports = reportService.getReportsByUserId(userId);
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ReportEntity>> getMyReports(HttpServletRequest request) throws NotFoundException {
        List<ReportEntity> reports = reportService.getReportsByUserId(request);
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }

    @GetMapping("/location/id/{locationId}")
    public ResponseEntity<List<ReportEntity>> getReportsByLocationId(@PathVariable int locationId) {
        List<ReportEntity> reports = reportService.getReportsByReportingLocationId(locationId);
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }

    @GetMapping("/location/name/{locationName}")
    public ResponseEntity<List<ReportEntity>> getReportsByLocationName(@PathVariable String locationName) {
        List<ReportEntity> reports = reportService.getReportsByReportingLocationName(locationName);
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }

    /*
     * @PutMapping("/{reportId}")
     * public ResponseEntity<ReportEntity> updateReport(@PathVariable int
     * reportId, @RequestBody ReportDTO reportDto, HttpServletRequest request) {
     * String jwt = request.getHeader("Authorization");
     * jwt= jwtService.removeBearerFromToken(jwt);
     * String username = jwtService.extractUsername(jwt);
     * UserEntity user = userService.getUserEntityByUsername(username);
     * try {
     * ReportEntity updatedReport = reportService.updateReport(reportId, reportDto,
     * user);
     * return new ResponseEntity<>(updatedReport, HttpStatus.OK);
     * } catch (Exception e) {
     * return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
     * }
     * }
     */

    @DeleteMapping("/{reportId}")
    public ResponseEntity<Void> deleteReport(@PathVariable int reportId, HttpServletRequest request)
            throws NotFoundException, NotAllowedException {
        reportService.deleteReport(reportId, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
