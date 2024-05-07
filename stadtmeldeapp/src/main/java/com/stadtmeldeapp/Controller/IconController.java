package com.stadtmeldeapp.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stadtmeldeapp.CustomExceptions.NotFoundException;
import com.stadtmeldeapp.DTO.IconDTO;
import com.stadtmeldeapp.Entity.IconEntity;
import com.stadtmeldeapp.service.IconService;

@RestController
@RequestMapping("/icons")
public class IconController {

    @Autowired
    private IconService iconService;

    @GetMapping("/{id}")
    public ResponseEntity<IconEntity> getIconById(@PathVariable int id) throws NotFoundException {
        IconEntity icon = iconService.findIconById(id);
        return new ResponseEntity<>(icon, HttpStatus.OK);
    }

    @GetMapping("/location/{locationName}")
    public ResponseEntity<List<IconDTO>> getIconByLocationName(@PathVariable String locationName)
            throws NotFoundException {
        List<IconDTO> icon = iconService.findIconByLocationName(locationName);
        return new ResponseEntity<>(icon, HttpStatus.OK);
    }
}
