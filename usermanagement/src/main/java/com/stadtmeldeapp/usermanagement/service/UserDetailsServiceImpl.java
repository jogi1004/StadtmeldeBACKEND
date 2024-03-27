package com.stadtmeldeapp.usermanagement.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.stadtmeldeapp.usermanagement.Entity.UserEntity;
import com.stadtmeldeapp.usermanagement.Repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    public UserDetailsServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserEntity user = repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("Nutzer mit Nutzernamen %s existiert nicht", username)));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }
}