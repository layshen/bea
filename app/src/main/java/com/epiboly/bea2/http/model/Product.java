package com.epiboly.bea2.http.model;

import java.math.BigDecimal;

public class Product {
    private Integer id;
    private String imageUrl;
    private String name;
    private BigDecimal price;
    private int quantity;
    private int score;

    public Product(String imageUrl, String name, BigDecimal price, int quantity) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Getter methods

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageUrl() { return imageUrl; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public int getQuantity() { return quantity; }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}