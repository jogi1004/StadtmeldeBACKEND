package com.stadtmeldeapp.DTO;

import com.stadtmeldeapp.Entity.StatusEntity;

public record ReportInfoDTO(String titleOrsubcategoryName, int iconId, StatusEntity status,
                String timestamp,
                Integer reportPictureId, double longitude, double latitude) {
}
