package com.stadtmeldeapp.DTO;

import java.util.Set;

import com.stadtmeldeapp.Entity.RoleEntity;

public record UserInfoDTO(
    int id,
    String username,
    String email,
    byte[] profilePicture,
    boolean notificationsEnabled,
    int reportingLocationId,
    Set<RoleEntity> roles
) {
}