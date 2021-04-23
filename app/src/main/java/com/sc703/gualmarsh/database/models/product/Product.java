package com.sc703.gualmarsh.database.models.product;

public class Product {

    public String code, name, description, category, expiration;
    public Long price, quantity;

    public Product() {
    }

    public Product(String code, String name, String description, Long price, Long quantity, String expiration) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.expiration = expiration;
    }
    public Product(String code, String name, String description, Long price, Long quantity, String expiration, String category) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
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

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
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
