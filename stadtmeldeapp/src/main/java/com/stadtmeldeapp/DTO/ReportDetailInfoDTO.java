package com.stadtmeldeapp.DTO;

import java.sql.Timestamp;

public record ReportDetailInfoDTO (String titleOrsubcategoryName, byte[] icon, Timestamp timestamp,  byte[] image, double longitude, double latitude, String username, byte[] userProfilePicture) {
    
}
