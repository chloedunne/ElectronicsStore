package com.example.electronicsstore;


import java.io.Serializable;

public class CardDetails implements Serializable {

    private long cardNumber;
    private int ccv;
    private int expiry;

    public CardDetails(){}

    public CardDetails(long cardNumber, int ccv, int expiry) {
        this.cardNumber = cardNumber;
        this.ccv = ccv;
        this.expiry = expiry;
    }

    public long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getCcv() {
        return ccv;
    }

    public void setCcv(int ccv) {
        this.ccv = ccv;
    }

    public int getExpiry() {
        return expiry;
    }

    public void setExpiry(int expiry) {
        this.expiry = expiry;
    }
}
