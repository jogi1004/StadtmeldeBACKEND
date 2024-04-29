package com.stadtmeldeapp.DTO;

import com.stadtmeldeapp.Entity.StatusEntity;

public record ReportDetailInfoDTO(String titleOrsubcategoryName, String description, int iconId, StatusEntity status,
                String timestamp,
                byte[] image, double longitude, double latitude, String username, String locationName, byte[] userProfilePicture) {
}
