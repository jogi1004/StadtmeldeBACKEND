package com.stadtmeldeapp.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.stadtmeldeapp.CustomExceptions.NotAllowedException;
import com.stadtmeldeapp.CustomExceptions.NotFoundException;
import com.stadtmeldeapp.DTO.RegisterDTO;
import com.stadtmeldeapp.DTO.UserInfoDTO;
import com.stadtmeldeapp.DTO.UserInfoNoProfilePictureDTO;
import com.stadtmeldeapp.Entity.ProfilePictureEntity;
import com.stadtmeldeapp.Entity.RoleEntity;
import com.stadtmeldeapp.Entity.UserEntity;
import com.stadtmeldeapp.Repository.ProfilePictureRepository;
import com.stadtmeldeapp.Repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final JwtService jwtService;
    private final ProfilePictureRepository imageRepository;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder, RoleService roleService,
            JwtService jwtService, ProfilePictureRepository imageRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.jwtService = jwtService;
        this.imageRepository = imageRepository;
    }

    @Transactional
    public UserEntity register(@Valid @RequestBody RegisterDTO request) throws NotFoundException, NotAllowedException {
        if (repository.existsByUsername(request.username()))
            throw new NotAllowedException("Nutzername existiert bereits");

        String hashedPassword = passwordEncoder.encode(request.password());
        RoleEntity role = roleService.findRoleByName("USER");

        UserEntity user = new UserEntity(request.username(), hashedPassword, request.email(),
                Collections.singletonList(role), null, null);

        return repository.save(user);
    }

    public UserInfoNoProfilePictureDTO getUserInfoByUsername(String username) throws NotFoundException {
        UserEntity user = repository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Nutzer nicht gefunden"));
        UserInfoNoProfilePictureDTO userInfoDTO = new UserInfoNoProfilePictureDTO(user.getId(), username,
                user.getEmail(),
                user.isNotificationsEnabled(), user.getRoles(), user.getAdminForLocation());
        return userInfoDTO;
    }

    public UserInfoDTO getUserInfoByUsernameWithProfilePicture(String username) throws NotFoundException {
        UserEntity user = repository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Nutzer nicht gefunden"));
        byte[] profilePic = null;
        if (user.getProfilePictureId() != null) {
            Optional<ProfilePictureEntity> image = imageRepository.findById(user.getProfilePictureId());
            if (image.isPresent()){
                profilePic = image.get().getImage();
            }  
        }

        UserInfoDTO userInfoDTO = new UserInfoDTO(user.getId(), username, user.getEmail(), profilePic,
                user.isNotificationsEnabled(), user.getRoles(), user.getAdminForLocation());
        return userInfoDTO;
    }

    public UserEntity getUserEntityByUsername(String username) throws NotFoundException {
        UserEntity user = repository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Nutzer nicht gefunden"));
        return user;
    }

    /*
     * @Transactional
     * public UserEntity updateUser(UserEntity userEntity) throws NotFoundException
     * {
     * UserEntity updatedUser = repository.findByUsername(userEntity.getUsername())
     * .orElseThrow(() -> new NotFoundException("Nutzer nicht gefunden"));
     * updatedUser.setEmail(userEntity.getEmail());
     * updatedUser.setNotificationsEnabled(userEntity.isNotificationsEnabled());
     * updatedUser.setAdminForLocation(userEntity.getAdminForLocation());
     * updatedUser.setProfilePictureId(userEntity.getProfilePictureId());
     * return repository.save(updatedUser);
     * }
     */

    @Transactional
    public void updateProfilePicture(byte[] image, HttpServletRequest request) throws NotFoundException {
        UserEntity user = getUserFromRequest(request);
        int imageId = imageRepository.save(new ProfilePictureEntity(image)).getId();
        user.setProfilePictureId(imageId);
        repository.save(user);
    }

    public boolean validate(String token, UserDetails userDetails) {
        token = jwtService.removeBearerFromToken(token);
        return jwtService.validateToken(token, userDetails);
    }

    public UserEntity getUserFromRequest(HttpServletRequest request) throws NotFoundException {
        String jwt = request.getHeader("Authorization");
        jwt = jwtService.removeBearerFromToken(jwt);
        String username = jwtService.extractUsername(jwt);
        UserEntity user = getUserEntityByUsername(username);
        return user;
    }

    public UserEntity getUserByAuthentication() throws NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getName().equals("anonymousUser")) {
            UserEntity userEntity = getUserEntityByUsername(authentication.getName());
            return userEntity;
        }
        return null;
    }

    @Transactional
    public void updateNotificationsEnabled(boolean notificationsEnabled, HttpServletRequest request) throws NotFoundException {
        UserEntity user = getUserFromRequest(request);
        user.setNotificationsEnabled(notificationsEnabled);
        repository.save(user);
    }
}