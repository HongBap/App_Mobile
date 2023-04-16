package com.example.app_mobile.Model;

import java.io.Serializable;
import java.util.Map;


public class Order implements Serializable {
    private Long orderId;
    private Long userId;
    private String orderPhone;
    private String orderAddress;
    private String orderStatus;
    private String orderTime;
    private Map<Long, Integer> orderDetails; //prodcutid và amount

    public Order() {
    }

    public Order(Long orderId, Long userId, String orderPhone, String orderAddress, String orderStatus, String orderTime, Map<Long, Integer> orderDetails) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderPhone = orderPhone;
        this.orderAddress = orderAddress;
        this.orderStatus = orderStatus;
        this.orderTime = orderTime;
        this.orderDetails = orderDetails;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOrderPhone() {
        return orderPhone;
    }

    public void setOrderPhone(String orderPhone) {
        this.orderPhone = orderPhone;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Map<Long, Integer> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(Map<Long, Integer> orderDetails) {
        this.orderDetails = orderDetails;
    }
}
