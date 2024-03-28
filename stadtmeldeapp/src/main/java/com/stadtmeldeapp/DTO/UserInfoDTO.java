package com.stadtmeldeapp.DTO;

public record UserInfoDTO(
    Long id,
    String username,
    String email,
    byte[] profilePicture,
    boolean notificationsEnabled,
    int reportingLocationId
) {
}