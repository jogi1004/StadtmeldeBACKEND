package com.stadtmeldeapp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stadtmeldeapp.CustomExceptions.NotFoundException;
import com.stadtmeldeapp.Entity.StatusEntity;
import com.stadtmeldeapp.Repository.StatusRepository;

import java.util.List;

@Service
@Transactional
public class StatusService {

    private final StatusRepository statusRepository;

    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public List<StatusEntity> getAllStatus() {
        return statusRepository.findAll();
    }

    public StatusEntity getStatusById(int id) throws NotFoundException {
        return statusRepository.findById(id).orElseThrow(() -> new NotFoundException("Status nicht gefunden"));
    }

    public StatusEntity createStatus(StatusEntity status) {
        return statusRepository.save(status);
    }

    public StatusEntity updateStatus(int id, StatusEntity updatedStatus) throws NotFoundException {
        StatusEntity status = statusRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Status nicht gefunden"));
        status.setName(updatedStatus.getName());
        return statusRepository.save(status);
    }

    public void deleteStatus(int id) {
        statusRepository.deleteById(id);
    }

    public List<StatusEntity> getStatusByReportingLocationId(int reportingLocationId) {
        return statusRepository.findByReportingLocationEntityId(reportingLocationId);
    }

}
