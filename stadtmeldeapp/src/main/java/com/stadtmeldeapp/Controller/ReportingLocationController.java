package com.stadtmeldeapp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stadtmeldeapp.CustomExceptions.NotFoundException;
import com.stadtmeldeapp.Entity.ReportingLocationEntity;
import com.stadtmeldeapp.service.ReportingLocationService;

@RestController
@RequestMapping("/location")
public class ReportingLocationController {

    @Autowired
    private ReportingLocationService locationService;

    @GetMapping("/{name}")
    public ResponseEntity<ReportingLocationEntity> getReportingLocationByName(@PathVariable String name) throws NotFoundException {
        ReportingLocationEntity location = locationService.getReportingLocationByName(name);
        return new ResponseEntity<>(location, HttpStatus.OK);

    }
}
