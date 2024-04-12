package com.stadtmeldeapp.DTO;

import java.sql.Timestamp;

public record ReportInfoDTO(String titleOrsubcategoryName, byte[] icon, Timestamp timestamp,  byte[] image, double longitude, double latitude) {
}
