package com.stadtmeldeapp.DTO;

import java.sql.Timestamp;

public record ReportInfoDTO(String titleOrsubcategoryName, byte[] icon, Timestamp timestamp,  byte[] image, double longitude, double latitude) {
    public String getTitleOrsubcategoryName(){
        return titleOrsubcategoryName;
    }

    public byte[] getIcon(){
        return icon;
    }

    public Timestamp getTimestamp(){
        return timestamp;
    }

    public byte[] getImage(){
        return image;
    }

    public double getLongitude(){
        return longitude;
    }

    public double getLatitude(){
        return latitude;
    }
}


