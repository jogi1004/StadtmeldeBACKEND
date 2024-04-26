package com.stadtmeldeapp.DTO;

public record ReportDTO(

        String title,
        String subCategoryName,
        String mainCategoryName,
        String description,
        Double longitude,
        Double latitude,
        String reportingLocationName,
        byte[] additionalPicture) {
}
