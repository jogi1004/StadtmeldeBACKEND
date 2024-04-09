package com.stadtmeldeapp.DTO;

public class SubcategoryDTO {

    private String title;
    private int mainCategoryID;
    
    public SubcategoryDTO(String title, int mainCategoryID) {
        this.title = title;
        this.mainCategoryID = mainCategoryID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMainCategoryID() {
        return mainCategoryID;
    }

    public void setMainCategoryID(int mainCategoryID) {
        this.mainCategoryID = mainCategoryID;
    }
    
}
