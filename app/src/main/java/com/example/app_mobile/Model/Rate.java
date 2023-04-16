package com.example.app_mobile.Model;

public class Rate {
    private Integer ratePoint;
    private String rateComment;
    private Long productProductId;
    private Long userId;
    private String userName;
    private String userEmail;

    public Rate(Integer ratePoint, String rateComment, Long productProductId, Long userId, String userName, String userEmail) {
        this.ratePoint = ratePoint;
        this.rateComment = rateComment;
        this.productProductId = productProductId;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public Rate() {
    }

    public Integer getRatePoint() {
        return ratePoint;
    }

    public void setRatePoint(Integer ratePoint) {
        this.ratePoint = ratePoint;
    }

    public String getRateComment() {
        return rateComment;
    }

    public void setRateComment(String rateComment) {
        this.rateComment = rateComment;
    }

    public Long getProductProductId() {
        return productProductId;
    }

    public void setProductProductId(Long productProductId) {
        this.productProductId = productProductId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}