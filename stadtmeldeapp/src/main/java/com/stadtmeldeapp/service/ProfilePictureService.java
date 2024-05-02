package com.stadtmeldeapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stadtmeldeapp.CustomExceptions.NotFoundException;
import com.stadtmeldeapp.Entity.ProfilePictureEntity;
import com.stadtmeldeapp.Repository.ProfilePictureRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProfilePictureService {
    
    @Autowired
    private ProfilePictureRepository profilePictureRepository;

    public ProfilePictureEntity getProfilePictureById(int id) throws NotFoundException {
        ProfilePictureEntity profilePictureEntity = profilePictureRepository.findById(id).orElseThrow(() -> new NotFoundException("Bild nicht gefunden"));
        return profilePictureEntity;
    }
}
