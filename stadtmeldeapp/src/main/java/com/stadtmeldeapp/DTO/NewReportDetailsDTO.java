package com.stadtmeldeapp.DTO;

import com.stadtmeldeapp.Entity.StatusEntity;

public record NewReportDetailsDTO(String description, StatusEntity status,
        String timestamp, String username) {
}
