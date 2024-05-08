package com.stadtmeldeapp.Controller;

import org.springframework.web.bind.annotation.RestController;

import com.stadtmeldeapp.CustomExceptions.NotAllowedException;
import com.stadtmeldeapp.CustomExceptions.NotFoundException;
import com.stadtmeldeapp.DTO.NewMainCategoryDTO;
import com.stadtmeldeapp.DTO.ReportInfoDTO;
import com.stadtmeldeapp.Entity.MaincategoryEntity;
import com.stadtmeldeapp.Entity.ReportingLocationEntity;
import com.stadtmeldeapp.Repository.MaincategoryRepository;
import com.stadtmeldeapp.service.ReportService;
import com.stadtmeldeapp.service.ReportingLocationService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class WebsiteRestController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ReportingLocationService reportingLocationService;

    @Autowired
    private MaincategoryRepository mainCategoryRepository;

    @GetMapping("/location/id/{locationId}")
    public ResponseEntity<List<ReportInfoDTO>> getReportsByLocationId(@PathVariable int locationId) {
        List<ReportInfoDTO> reports = reportService.getReportsByReportingLocationId(locationId);
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }

    @PostMapping("/maincategory")
    public ResponseEntity<Void> createOrUpdateMainCategory(@RequestBody @NonNull NewMainCategoryDTO mainCategoryDTO) throws NotFoundException, NotAllowedException {
        ReportingLocationEntity reportingLocationEntity = reportingLocationService.getReportingLocationById(mainCategoryDTO.reportingLocationId());
        MaincategoryEntity maincategoryEntity = new MaincategoryEntity();
        maincategoryEntity.setTitle(mainCategoryDTO.title());
        maincategoryEntity.setReportingLocationEntity(reportingLocationEntity);
        maincategoryEntity.setIconEntity(null);
        maincategoryEntity.setIconId(null);
        mainCategoryRepository.save(maincategoryEntity);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
