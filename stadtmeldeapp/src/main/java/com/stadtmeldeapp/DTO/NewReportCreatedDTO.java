package com.stadtmeldeapp.DTO;

import com.stadtmeldeapp.Entity.ReportingLocationEntity;
import com.stadtmeldeapp.Entity.StatusEntity;
import com.stadtmeldeapp.Entity.SubcategoryEntity;
import java.sql.Timestamp;

public record NewReportCreatedDTO(int id, SubcategoryEntity subcategoryEntity, int userId, String title,
        String description, Double longitude, Double latitude, Timestamp reportingTimestamp, Integer reportPictureId,
        ReportingLocationEntity reportingLocationEntity, StatusEntity statusEntity) {
}
