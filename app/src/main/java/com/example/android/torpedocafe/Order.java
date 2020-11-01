package com.example.android.torpedocafe;

import java.util.ArrayList;

public class Order {
    ArrayList<String> names;
    ArrayList<Double> prices;
    ArrayList<Integer> quantity;
    ArrayList<Integer> stocks;

    public ArrayList<String> getNames() {
        return names;
    }

    public void setNames(ArrayList<String> names) {
        this.names = names;
    }

    public ArrayList<Double> getPrices() {
        return prices;
    }

    public void setPrices(ArrayList<Double> prices) {
        this.prices = prices;
    }

    public ArrayList<Integer> getQuantity() {
        return quantity;
    }

    public void setQuantity(ArrayList<Integer> quantity) {
        this.quantity = quantity;
    }

    public ArrayList<Integer> getStocks() {
        return stocks;
    }

    public void setStocks(ArrayList<Integer> stocks) {
        this.stocks = stocks;
    }
}
