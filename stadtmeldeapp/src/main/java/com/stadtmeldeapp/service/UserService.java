package com.stadtmeldeapp.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.stadtmeldeapp.DTO.RegisterDTO;
import com.stadtmeldeapp.DTO.UserInfoDTO;
import com.stadtmeldeapp.Entity.RoleEntity;
import com.stadtmeldeapp.Entity.UserEntity;
import com.stadtmeldeapp.Repository.UserRepository;

import jakarta.validation.Valid;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final JwtService jwtService;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder, RoleService roleService,
            JwtService jwtService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.jwtService = jwtService;
    }

    @Transactional
    public UserEntity register(@Valid @RequestBody RegisterDTO request) {
        Optional<UserEntity> existingUser = repository.findByUsername(request.username());
        if (existingUser.isPresent()) {
            return null;
        }

        String hashedPassword = passwordEncoder.encode(request.password());
        RoleEntity role = roleService.findRoleByName("USER");

        UserEntity user = new UserEntity(request.username(), hashedPassword, request.email(),
                Collections.singleton(role));

        return repository.save(user);
    }

    public UserInfoDTO getUserInfoByUsername(String username) {
        Optional<UserEntity> newUser = repository.findByUsername(username);
        if (newUser.isPresent()) {
            UserEntity user = newUser.get();
            UserInfoDTO userInfoDTO = new UserInfoDTO(user.getId(), username, user.getEmail(), user.getProfilePicture(),
                    user.isNotificationsEnabled(), user.getRoles());
            return userInfoDTO;
        }
        return null;
    }

    public UserEntity getUserEntityByUsername(String username) {
        Optional<UserEntity> newUser = repository.findByUsername(username);
        if (newUser.isPresent()) {
            UserEntity user = newUser.get();
            return user;
        }
        return null;
    }

    @Transactional
    public UserEntity updateUser(UserEntity userEntity){
        UserEntity updatedUser = repository.findByUsername(userEntity.getUsername()).orElse(null);
        
        if(updatedUser != null){
            updatedUser.setProfilePicture(userEntity.getProfilePicture());
            return repository.save(updatedUser);
        }
        return null;
    }

    public boolean validate(String token, UserDetails userDetails) {
        token = jwtService.removeBearerFromToken(token);
        return jwtService.validateToken(token, userDetails);
    }
}