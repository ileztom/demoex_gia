package org.example.demo_ex.model;

import java.time.LocalDate;

public class Order {
    private int orderId;
    private String article;
    private String status;
    private String pickupPoint;
    private LocalDate orderDate;
    private LocalDate receiveDate;

    public Order() {}

    public Order(int orderId, String article, String status, String pickupPoint,
                 LocalDate orderDate, LocalDate receiveDate) {
        this.orderId = orderId;
        this.article = article;
        this.status = status;
        this.pickupPoint = pickupPoint;
        this.orderDate = orderDate;
        this.receiveDate = receiveDate;
    }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public String getArticle() { return article; }
    public void setArticle(String article) { this.article = article; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPickupPoint() { return pickupPoint; }
    public void setPickupPoint(String pickupPoint) { this.pickupPoint = pickupPoint; }
    public LocalDate getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }
    public LocalDate getReceiveDate() { return receiveDate; }
    public void setReceiveDate(LocalDate receiveDate) { this.receiveDate = receiveDate; }
}
