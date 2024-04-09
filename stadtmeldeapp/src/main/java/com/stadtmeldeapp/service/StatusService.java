package com.stadtmeldeapp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stadtmeldeapp.Entity.StatusEntity;
import com.stadtmeldeapp.Repository.StatusRepository;

import java.util.List;
import java.util.Optional;

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

    public Optional<StatusEntity> getStatusById(int id) {
        return statusRepository.findById(id);
    }

    public StatusEntity createStatus(StatusEntity status) {
        return statusRepository.save(status);
    }

    public StatusEntity updateStatus(int id, StatusEntity updatedStatus) {
        Optional<StatusEntity> statusOptional = statusRepository.findById(id);
        if (statusOptional.isPresent()) {
            StatusEntity status = statusOptional.get();
            status.setName(updatedStatus.getName());
            return statusRepository.save(status);
        } else {
            throw new RuntimeException("Status not found with id: " + id);
        }
    }

    public void deleteStatus(int id) {
        statusRepository.deleteById(id);
    }

    public List<StatusEntity> getStatusByReportingLocationId(int reportingLocationId) {
        return statusRepository.findByReportingLocationEntityId(reportingLocationId);
    }

}
