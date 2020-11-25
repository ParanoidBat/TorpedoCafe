package com.example.android.torpedocafe;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

// https://torpedo-cafe-7bd4c.firebaseio.com/ @waynetech010

public class FirebaseController {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;

    public void addNewItem(Item item){
        // this method will work for updating price and stock as well

        databaseReference = firebaseDatabase.getReference("Items");

        databaseReference.child(item.getName()).setValue(item);

        databaseReference = null;
    }

    public void updateStock(String name, int amount){
        databaseReference = firebaseDatabase.getReference("Items");

        databaseReference.child(name).child("stock").setValue(amount);

        databaseReference = null;
    }

    public void salePerOrder(int sale, ArrayList<String> names, ArrayList<Integer> quantity){
        databaseReference = firebaseDatabase.getReference("Sale");

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String date = format.format(new Date());

        databaseReference = databaseReference.child(date);

        // push sale revenue
        databaseReference.push().setValue(sale);

        // push sold stock
        databaseReference = firebaseDatabase.getReference("Sold").child(date).push();

        for(int i = 0; i < names.size(); i++){
            databaseReference.child(names.get(i)).setValue(quantity.get(i));
        }

        databaseReference = null;
    }

    public void deleteItem(String name){
        databaseReference = FirebaseDatabase.getInstance().getReference("Items");

        databaseReference.child(name).setValue(null);

        databaseReference = null;
    }
}
