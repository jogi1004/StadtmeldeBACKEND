package com.stadtmeldeapp.service;

import com.stadtmeldeapp.CustomExceptions.NotFoundException;
import com.stadtmeldeapp.Entity.RoleEntity;
import com.stadtmeldeapp.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    
    @Autowired
    private RoleRepository roleRepository;

    public RoleEntity findRoleByName(String roleName) throws NotFoundException {
        return roleRepository.findByName(roleName).orElseThrow(() -> new NotFoundException("Rolle nicht gefunden"));
    }
}