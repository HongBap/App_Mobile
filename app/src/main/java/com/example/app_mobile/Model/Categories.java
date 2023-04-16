package com.example.app_mobile.Model;

public class Categories {
    int cateId;
    String categoryName;
    String description;
    String hinhanhsp;

    public Categories(int cateId, String categoryName, String hinhanhsp) {
        this.cateId = cateId;
        this.categoryName = categoryName;
        this.hinhanhsp = hinhanhsp;
    }

    public Categories(int cateId, String hinhanhsp) {
        this.cateId = cateId;
        this.hinhanhsp = hinhanhsp;
    }

    public int getCateId() {
        return cateId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHinhanhsp() {
        return hinhanhsp;
    }

    public void setHinhanhsp(String hinhanhsp) {
        this.hinhanhsp = hinhanhsp;
    }
}
