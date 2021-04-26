package com.sc703.gualmarsh.database.models.product;

public class Product {

    public String code, name, description, category, expiration, lastUpdated;
    public Long unitPrice, totalPrice, quantity;

    public Product() {
    }

    public Product(String code, String name, String description, Long unitPrice, Long totalPrice, Long quantity, String expiration, String lastUpdated) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.quantity = quantity;
        this.expiration = expiration;
        this.lastUpdated = lastUpdated;
    }
    public Product(String code, String name, String description, Long unitPrice, Long totalPrice, Long quantity, String expiration, String lastUpdated, String category) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.quantity = quantity;
        this.category = category;
        this.lastUpdated = lastUpdated;
        this.expiration = expiration;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Long getUnitPrice() {
        return unitPrice;
    }
    public Long getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }
    public void setUnitPrice(Long unitPrice) {
        this.unitPrice = unitPrice;
    }
    public Long getQuantity() {
        return quantity;
    }
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
}
