package com.stadtmeldeapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stadtmeldeapp.CustomExceptions.NotAllowedException;
import com.stadtmeldeapp.CustomExceptions.NotFoundException;
import com.stadtmeldeapp.Entity.ReportingLocationEntity;
import com.stadtmeldeapp.Entity.StatusEntity;
import com.stadtmeldeapp.Repository.StatusRepository;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@Service
@Transactional
public class StatusService {

    @Autowired
    private UserService userService;
    
    private final StatusRepository statusRepository;

    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public List<StatusEntity> getAllStatus(HttpServletRequest request) throws NotFoundException {
        int locationId = userService.getUserFromRequest(request).getAdminForLocation().getId();
        return statusRepository.findByReportingLocationEntityId(locationId);
    }

    public StatusEntity getStatusById(int id, HttpServletRequest request) throws NotFoundException {
        return statusRepository.findById(id).orElseThrow(() -> new NotFoundException("Status nicht gefunden"));
    }

    public StatusEntity createStatus(StatusEntity status, HttpServletRequest request) throws NotFoundException, NotAllowedException {
        ReportingLocationEntity isAdminForLocation = userService.getUserFromRequest(request).getAdminForLocation();
        if (!status.getReportingLocationEntity().equals(isAdminForLocation)) throw new NotAllowedException("Keine Berechtigung");
        return statusRepository.save(status);
    }

    public StatusEntity updateStatus(int id, StatusEntity updatedStatus, HttpServletRequest request) throws NotFoundException, NotAllowedException {
        StatusEntity status = statusRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Status nicht gefunden"));
        status.setName(updatedStatus.getName());
        ReportingLocationEntity isAdminForLocation = userService.getUserFromRequest(request).getAdminForLocation();
        if (!updatedStatus.getReportingLocationEntity().equals(isAdminForLocation)) throw new NotAllowedException("Keine Berechtigung");
        return statusRepository.save(status);
    }

    public void deleteStatus(int id, HttpServletRequest request) throws NotFoundException, NotAllowedException {
        ReportingLocationEntity isAdminForLocation = userService.getUserFromRequest(request).getAdminForLocation();
        ReportingLocationEntity statusLocation = getStatusById(id, request).getReportingLocationEntity();
        if (!statusLocation.equals(isAdminForLocation)) throw new NotAllowedException("Keine Berechtigung");
        statusRepository.deleteById(id);
    }
}
