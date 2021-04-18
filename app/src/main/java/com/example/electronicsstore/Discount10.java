package com.example.electronicsstore;

public class Discount10 implements DiscountCalculator{
    @Override
    public double calculateDiscount(double total) {
        return total * 0.9;
    }
}
