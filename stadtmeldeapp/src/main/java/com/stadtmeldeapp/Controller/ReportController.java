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
import com.stadtmeldeapp.DTO.ReportInfoDTO;
import com.stadtmeldeapp.Entity.ReportEntity;
import com.stadtmeldeapp.service.ReportService;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;


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

    @GetMapping("/{id}")
    public ResponseEntity<ReportDetailInfoDTO> getReportDetails(@PathVariable int id) throws NotFoundException {
        ReportDetailInfoDTO report = reportService.getReportDetails(id);
        return new ResponseEntity<>(report, HttpStatus.OK);
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
