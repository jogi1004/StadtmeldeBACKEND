package com.stadtmeldeapp.DTO;

public class MaincategoryDTO {

    private String title;
    private int reportingLocationID;

    public MaincategoryDTO(String title, int reportingLocationID) {
        this.title = title;
        this.reportingLocationID = reportingLocationID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getreportingLocationID() {
        return reportingLocationID;
    }

    public void setreportingLocationID(int reportingLocationID) {
        this.reportingLocationID = reportingLocationID;
    }

}
