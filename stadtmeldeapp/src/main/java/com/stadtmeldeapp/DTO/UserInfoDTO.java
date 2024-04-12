package com.stadtmeldeapp.DTO;

import java.util.List;

import com.stadtmeldeapp.Entity.ReportingLocationEntity;
import com.stadtmeldeapp.Entity.RoleEntity;

public record UserInfoDTO(
        int id,
        String username,
        String email,
        byte[] profilePicture,
        boolean notificationsEnabled,
        List<RoleEntity> roles,
        ReportingLocationEntity adminForLocation) {
}