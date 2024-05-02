package com.stadtmeldeapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stadtmeldeapp.CustomExceptions.NotFoundException;
import com.stadtmeldeapp.Entity.ReportPictureEntity;
import com.stadtmeldeapp.Repository.ReportPictureRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReportPictureService {
    
    @Autowired
    private ReportPictureRepository reportPictureRepository;

    public ReportPictureEntity getReportPictureById(int id) throws NotFoundException {
        ReportPictureEntity reportPictureEntity = reportPictureRepository.findById(id).orElseThrow(() -> new NotFoundException("Bild nicht gefunden"));
        return reportPictureEntity;
    }
}
