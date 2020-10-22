package com.example.android.torpedocafe;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
}
