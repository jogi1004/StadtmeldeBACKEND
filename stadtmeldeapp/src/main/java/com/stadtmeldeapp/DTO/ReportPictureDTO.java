package com.stadtmeldeapp.DTO;

import java.sql.Timestamp;

import com.stadtmeldeapp.Entity.StatusEntity;

public class ReportPictureDTO {

    private int id;
    private String titleOrsubcategoryName;
    private String icon;
    private Timestamp timestamp;
    private Integer reportPictureId;
    private double longitude;
    private double latitude;
    private StatusEntity status;

    public ReportPictureDTO(int id, String titleOrsubcategoryName, String icon, Timestamp timestamp, Integer reportPictureId,
            double longitude, double latitude, StatusEntity status) {
        this.id = id;
        this.titleOrsubcategoryName = titleOrsubcategoryName;
        this.icon = icon;
        this.timestamp = timestamp;
        this.reportPictureId = reportPictureId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StatusEntity getStatus() {
        return status;
    }

    public void setStatus(StatusEntity status) {
        this.status = status;
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

    public Integer getReportPictureId() {
        return reportPictureId;
    }

    public void setReportPictureId(Integer reportPictureId) {
        this.reportPictureId = reportPictureId;
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
