package com.stadtmeldeapp.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.stadtmeldeapp.CustomExceptions.NotAllowedException;
import com.stadtmeldeapp.CustomExceptions.NotFoundException;
import com.stadtmeldeapp.DTO.RegisterDTO;
import com.stadtmeldeapp.DTO.UserInfoDTO;
import com.stadtmeldeapp.Entity.RoleEntity;
import com.stadtmeldeapp.Entity.UserEntity;
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

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder, RoleService roleService,
            JwtService jwtService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.jwtService = jwtService;
    }

    public UserEntity register(@Valid @RequestBody RegisterDTO request) throws NotFoundException, NotAllowedException {
        Optional<UserEntity> existingUser = repository.findByUsername(request.username());
        if (existingUser.isPresent()) {
            throw new NotAllowedException("Nutzername existiert bereits");
        }

        String hashedPassword = passwordEncoder.encode(request.password());
        RoleEntity role = roleService.findRoleByName("USER");

        UserEntity user = new UserEntity(request.username(), hashedPassword, request.email(),
                Collections.singleton(role), null);

        return repository.save(user);
    }

    public UserInfoDTO getUserInfoByUsername(String username) throws NotFoundException {
        UserEntity user = repository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Nutzer nicht gefunden"));
        UserInfoDTO userInfoDTO = new UserInfoDTO(user.getId(), username, user.getEmail(), user.getProfilePicture(),
                user.isNotificationsEnabled(), user.getRoles(), user.getAdminForLocation());
        return userInfoDTO;
    }

    public UserEntity getUserEntityByUsername(String username) throws NotFoundException {
        UserEntity user = repository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Nutzer nicht gefunden"));
        return user;
    }

    @Transactional
    public UserEntity updateUser(UserEntity userEntity) throws NotFoundException {
        UserEntity updatedUser = repository.findByUsername(userEntity.getUsername())
                .orElseThrow(() -> new NotFoundException("Nutzer nicht gefunden"));
            updatedUser.setProfilePicture(userEntity.getProfilePicture());
            return repository.save(updatedUser);
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
}