package com.example.app_mobile.Model;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {
    private int productId;
    private int productPrice;
    private String productName;
    private String productDescription;
    private int cateId;
    private String categoryName;
    private String productCreateDate;
    private int productRemain;
    private String productUpDate;
    private List<Integer> featureIds;

    private List<String> imageUrls;

    public Product(int productId, int productPrice, String productName, String productDescription, int cateId, String categoryName, String productCreateDate, int productRemain, String productUpDate, List<Integer> featureIds, List<String> imageUrls) {
        this.productId = productId;
        this.productPrice = productPrice;
        this.productName = productName;
        this.productDescription = productDescription;
        this.cateId = cateId;
        this.categoryName = categoryName;
        this.productCreateDate = productCreateDate;
        this.productRemain = productRemain;
        this.productUpDate = productUpDate;
        this.featureIds = featureIds;
        this.imageUrls = imageUrls;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
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

    public String getProductCreateDate() {
        return productCreateDate;
    }

    public void setProductCreateDate(String productCreateDate) {
        this.productCreateDate = productCreateDate;
    }

    public int getProductRemain() {
        return productRemain;
    }

    public void setProductRemain(int productRemain) {
        this.productRemain = productRemain;
    }

    public String getProductUpDate() {
        return productUpDate;
    }

    public void setProductUpDate(String productUpDate) {
        this.productUpDate = productUpDate;
    }

    public List<Integer> getFeatureIds() {
        return featureIds;
    }

    public void setFeatureIds(List<Integer> featureIds) {
        this.featureIds = featureIds;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}

