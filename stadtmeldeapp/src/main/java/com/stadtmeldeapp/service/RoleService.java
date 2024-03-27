/* package com.stadtmeldeapp.service;

import com.stadtmeldeapp.Entity.RoleEntity;
import com.stadtmeldeapp.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    
    @Autowired
    private RoleRepository roleRepository;

    public RoleEntity findRoleByName(String roleName) {
        Optional<RoleEntity> role = roleRepository.findByName(roleName);
        if (role.isPresent()) {
            return role.get();
        }
        return null;
    }
}
 */