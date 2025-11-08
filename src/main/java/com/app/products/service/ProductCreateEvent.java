package com.app.products.service;

import java.math.BigDecimal;

public class ProductCreateEvent {

     private String title;
     private BigDecimal price;
     private Integer quantity;
     private String productId;

    public ProductCreateEvent() {}

    public ProductCreateEvent(String title, BigDecimal price, Integer quantity, String productId) {
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
