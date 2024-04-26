package com.stadtmeldeapp.service;

import com.stadtmeldeapp.CustomExceptions.NotFoundException;
import com.stadtmeldeapp.Entity.IconEntity;
import com.stadtmeldeapp.Repository.IconRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IconService {
    
    @Autowired
    private IconRepository iconRepository;

    public IconEntity findIconById(int id) throws NotFoundException {
        return iconRepository.findById(id).orElseThrow(() -> new NotFoundException("Icon nicht gefunden"));
    }
}