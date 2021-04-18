package com.example.electronicsstore.objects;

import java.io.Serializable;

public class Product implements Serializable {

    String title, manufacturer, category, id;
    Double price;
    int stock;

    public Product(){}

    public Product(String title, String manufacturer, String category, String id, Double price, int stock) {
        this.title = title;
        this.manufacturer = manufacturer;
        this.category = category;
        this.id = id;
        this.price = price;
        this.stock = stock;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
