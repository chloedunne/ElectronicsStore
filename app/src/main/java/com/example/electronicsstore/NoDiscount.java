package com.example.electronicsstore;

public class NoDiscount implements DiscountCalculator {
    @Override
    public double calculateDiscount(double total) {
        return total;
    }
}
