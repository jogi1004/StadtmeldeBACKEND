package com.stadtmeldeapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stadtmeldeapp.CustomExceptions.NotAllowedException;
import com.stadtmeldeapp.CustomExceptions.NotFoundException;
import com.stadtmeldeapp.DTO.ProfilePictureDTO;
import com.stadtmeldeapp.Entity.ProfilePictureEntity;
import com.stadtmeldeapp.Repository.ProfilePictureRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProfilePictureService {
    
    @Autowired
    private ProfilePictureRepository profilePictureRepository;

    public ProfilePictureDTO getProfilePictureById(int id, HttpServletRequest request) throws NotFoundException, NotAllowedException {
        ProfilePictureEntity profilePictureEntity = profilePictureRepository.findById(id).orElseThrow(() -> new NotFoundException("Bild nicht gefunden"));
        return new ProfilePictureDTO(id, profilePictureEntity.getImage());
    }
}
