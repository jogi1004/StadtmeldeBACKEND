package com.stadtmeldeapp.DTO;

public record ReportUpdateDTO(

        String title,
        String description,
        byte[] additionalPicture) {
}
