package com.londonappbrewery.bitcointicker.model;

public class CoinModel {

    private double value;
    private String currency;

    public CoinModel(double value, String currency){
        this.value = value;
        this.currency = currency;
    }

    public String getFullCurency(){
        return String.valueOf(value) + " " + currency;
    }
}
