package com.example.android.torpedocafe;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
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

    public void salePerOrder(int sale){
        databaseReference = firebaseDatabase.getReference("Sale");

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String date = format.format(new Date());

        databaseReference = databaseReference.child(date);

        databaseReference.push().setValue(sale);

        databaseReference = null;
    }
}
