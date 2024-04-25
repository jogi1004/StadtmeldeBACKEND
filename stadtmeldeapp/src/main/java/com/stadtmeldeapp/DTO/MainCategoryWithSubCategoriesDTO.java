package com.stadtmeldeapp.DTO;

import java.util.List;

import com.stadtmeldeapp.Entity.MaincategoryEntity;
import com.stadtmeldeapp.Entity.ReportingLocationEntity;
import com.stadtmeldeapp.Entity.SubcategoryEntity;

public class MainCategoryWithSubCategoriesDTO {

    private int id;
    private String title;
    private ReportingLocationEntity reportingLocationEntity;
    private List<SubcategoryEntity> subCategories;

    public MainCategoryWithSubCategoriesDTO(MaincategoryEntity maincategoryEntity, List<SubcategoryEntity> subcategoryEntities){
        this.id = maincategoryEntity.getId();
        this.title = maincategoryEntity.getTitle();
        this.reportingLocationEntity = maincategoryEntity.getReportingLocationEntity();
        this.subCategories = subcategoryEntities;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public ReportingLocationEntity getReportingLocationEntity() {
        return reportingLocationEntity;
    }
    public void setReportingLocationEntity(ReportingLocationEntity reportingLocationEntity) {
        this.reportingLocationEntity = reportingLocationEntity;
    }
    public List<SubcategoryEntity> getSubCategories() {
        return subCategories;
    }
    public void setSubCategories(List<SubcategoryEntity> subCategories) {
        this.subCategories = subCategories;
    }

    
}
