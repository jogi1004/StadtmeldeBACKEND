package com.stadtmeldeapp.DTO;

import java.sql.Timestamp;

public class ReportPictureDTO {
    
private String titleOrsubcategoryName;
private String icon;
private Timestamp timestamp;
private String image;
private double longitude;
private double latitude;

public ReportPictureDTO(String titleOrsubcategoryName, String icon, Timestamp timestamp, String image, double longitude, double latitude) {
    this.titleOrsubcategoryName = titleOrsubcategoryName;
    this.icon = icon;
    this.timestamp = timestamp;
    this.image = image;
    this.longitude = longitude;
    this.latitude = latitude;
}

public String getTitleOrsubcategoryName() {
    return titleOrsubcategoryName;
}

public void setTitleOrsubcategoryName(String titleOrsubcategoryName) {
    this.titleOrsubcategoryName = titleOrsubcategoryName;
}

public String getIcon() {
    return icon;
}

public void setIcon(String icon) {
    this.icon = icon;
}

public Timestamp getTimestamp() {
    return timestamp;
}

public void setTimestamp(Timestamp timestamp) {
    this.timestamp = timestamp;
}

public String getImage() {
    return image;
}

public void setImage(String image) {
    this.image = image;
}

public double getLongitude() {
    return longitude;
}

public void setLongitude(double longitude) {
    this.longitude = longitude;
}

public double getLatitude() {
    return latitude;
}

public void setLatitude(double latitude) {
    this.latitude = latitude;
}

}


