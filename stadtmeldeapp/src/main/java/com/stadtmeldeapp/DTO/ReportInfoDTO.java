package com.stadtmeldeapp.DTO;

import java.sql.Timestamp;

import com.stadtmeldeapp.Entity.StatusEntity;

public record ReportInfoDTO(String titleOrsubcategoryName, int iconId, StatusEntity status, Timestamp timestamp,
        byte[] image, double longitude, double latitude) {
}


