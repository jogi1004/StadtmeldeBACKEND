package com.stadtmeldeapp.DTO;

import java.util.List;

import com.stadtmeldeapp.Entity.ReportingLocationEntity;
import com.stadtmeldeapp.Entity.RoleEntity;

public record UserInfoNoProfilePictureDTO(
        int id,
        String username,
        String email,
        boolean notificationsEnabled,
        List<RoleEntity> roles,
        ReportingLocationEntity adminForLocation) {
}