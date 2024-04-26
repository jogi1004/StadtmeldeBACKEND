package com.stadtmeldeapp.Controller;

import org.springframework.web.bind.annotation.RestController;
import com.stadtmeldeapp.DTO.ReportDetailInfoDTO;
import com.stadtmeldeapp.service.ReportService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
public class WebsiteRestController {

    @Autowired
    private ReportService reportService;
    
    @GetMapping("/location/id/{locationId}")
    public ResponseEntity<List<ReportDetailInfoDTO>> getReportsByLocationId(@PathVariable int locationId) {
        List<ReportDetailInfoDTO> reports = reportService.getReportsByReportingLocationId(locationId);
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }
    
}
