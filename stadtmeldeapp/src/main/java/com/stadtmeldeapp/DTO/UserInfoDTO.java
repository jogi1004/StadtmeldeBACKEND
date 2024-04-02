package com.stadtmeldeapp.DTO;

public record UserInfoDTO(
    int id,
    String username,
    String email,
    byte[] profilePicture,
    boolean notificationsEnabled,
    int reportingLocationId
) {
}