package com.stadtmeldeapp.usermanagement.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stadtmeldeapp.usermanagement.DTO.RegisterDTO;
import com.stadtmeldeapp.usermanagement.DTO.UserInfoDTO;
import com.stadtmeldeapp.usermanagement.Entity.RoleEntity;
import com.stadtmeldeapp.usermanagement.Entity.UserEntity;
import com.stadtmeldeapp.usermanagement.Repository.UserRepository;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final JwtService jwtService;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder, RoleService roleService, JwtService jwtService) {
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
        RoleEntity role = roleService.findRoleByName("ROLE_USER");

        UserEntity user = new UserEntity(request.username(), hashedPassword, request.firstname(), request.lastname(),
                request.street(), request.housenumber(), request.zipcode(), request.city(),
                Collections.singleton(role));

        return repository.save(user);
    }

    public UserInfoDTO getUserInfoByUsername(String username) {
        Optional<UserEntity> newUser = repository.findByUsername(username);
        if (newUser.isPresent()) {
            UserEntity user = newUser.get();
            UserInfoDTO userInfoDTO = new UserInfoDTO();
            userInfoDTO.setId(user.getId());
            userInfoDTO.setFirstname(user.getFirstname());
            userInfoDTO.setLastname(user.getLastname());
            userInfoDTO.setUsername(user.getUsername());
            userInfoDTO.setStreet(user.getStreet());
            userInfoDTO.setHousenumber(user.getHousenumber());
            userInfoDTO.setZipcode(user.getZipcode());
            userInfoDTO.setCity(user.getCity());
            return userInfoDTO;
        }
        return null;
    }

    public boolean validate(String token, UserDetails userDetails) {
        token = jwtService.removeBearerFromToken(token);
        return jwtService.validateToken(token, userDetails);
    }
}