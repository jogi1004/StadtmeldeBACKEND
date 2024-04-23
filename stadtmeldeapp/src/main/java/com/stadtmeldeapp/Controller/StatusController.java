package com.stadtmeldeapp.Controller;

import com.stadtmeldeapp.CustomExceptions.NotAllowedException;
import com.stadtmeldeapp.CustomExceptions.NotFoundException;
import com.stadtmeldeapp.Entity.StatusEntity;
import com.stadtmeldeapp.service.StatusService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/status")
public class StatusController {

    @Autowired
    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<StatusEntity>> getAllStatus() {
        List<StatusEntity> statusList = statusService.getAllStatus();
        return new ResponseEntity<>(statusList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatusEntity> getStatusById(@PathVariable("id") int id) throws NotFoundException {
        StatusEntity status = statusService.getStatusById(id);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<StatusEntity> createStatus(@RequestBody StatusEntity status, HttpServletRequest request) throws NotFoundException, NotAllowedException {
        StatusEntity createdStatus = statusService.createStatus(status, request);
        return new ResponseEntity<>(createdStatus, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StatusEntity> updateStatus(@PathVariable("id") int id,
            @RequestBody StatusEntity updatedStatus, HttpServletRequest request) throws NotFoundException, NotAllowedException {
        StatusEntity updated = statusService.updateStatus(id, updatedStatus, request);
        return new ResponseEntity<>(updated, HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatus(@PathVariable("id") int id, HttpServletRequest request) throws NotFoundException, NotAllowedException {
        statusService.deleteStatus(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
