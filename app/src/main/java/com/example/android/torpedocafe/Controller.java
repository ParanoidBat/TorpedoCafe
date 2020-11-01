package com.example.android.torpedocafe;

import android.util.Log;

import java.util.ArrayList;

public class Controller {

    public void addStock(Item item, String stock){
        // add in existing stock

        int oldStock = item.getStock();
        int newStock = Integer.parseInt(stock);

        item.setStock(oldStock + newStock);
    }

    public boolean updateOrder(Order order, Item item, int quantity){
        // if order is empty, just add stuff
        try{
            if(order.getNames().isEmpty()){
                // counting on it to throw exception
            }
        }
        catch (Exception e){
            e.printStackTrace();

            order.stocks = new ArrayList<>();
            order.names = new ArrayList<>();
            order.prices = new ArrayList<>();
            order.quantity = new ArrayList<>();

            order.names.add(item.getName());
            order.prices.add(item.getPrice());
            order.stocks.add(item.getStock()); // need stock info for updating stock in database

            if(quantity <= item.getStock()){
                order.quantity.add(quantity);
            }
            else{
                order.quantity.add(0);
                return false;
            }

            return true;
        }

        // if order item exists, update it
        int index = order.getNames().indexOf(item.getName());

        if(index < 0){
            // item doesn't exist, create new item
            order.names.add(item.getName());
            order.prices.add(item.getPrice());
            order.stocks.add(item.getStock());

            if(quantity <= item.getStock()){
                order.quantity.add(quantity);
            }
            else{
                order.quantity.add(0);
                return false;
            }
        }
        else{
            if(quantity <= item.getStock())
                order.quantity.set(index, quantity);
            else{
                order.quantity.set(index, 0);
                return false;
            }
        }

        return true;
    }

    public int confirmOrder(Order order){
        if(order.getNames().isEmpty()) return -1;

        FirebaseController fc = new FirebaseController();

        ArrayList<String> names = order.getNames();
        ArrayList<Double> prices = order.getPrices();
        ArrayList<Integer> quantity = order.getQuantity();
        ArrayList<Integer> stocks = order.getStocks();

        int bill = 0;

        for(int i = 0; i < names.size(); i++){
            fc.updateStock(names.get(i), stocks.get(i) - quantity.get(i) );

            bill += (prices.get(i)*quantity.get(i));
        }

        // reset order instance
        order.stocks.clear();
        order.names.clear();
        order.quantity.clear();
        order.prices.clear();

        return bill;
    }
}
