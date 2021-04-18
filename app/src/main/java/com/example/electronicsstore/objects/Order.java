package com.example.electronicsstore.objects;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable {

    private ArrayList<Product> products;
    private Profile profile;
    private double total;
    private String orderNum;

    public Order(){}

    public Order(ArrayList<Product> products, Profile profile, double total, String orderNum) {
        this.products = products;
        this.profile = profile;
        this.total = total;
        this.orderNum = orderNum;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
