package com.example.android.torpedocafe;

public class Controller {

    public void updateStock(Item item, String stock){
        // add in existing stock

        int oldStock = item.getStock();
        int newStock = Integer.parseInt(stock);

        item.setStock(oldStock + newStock);
    }
}
