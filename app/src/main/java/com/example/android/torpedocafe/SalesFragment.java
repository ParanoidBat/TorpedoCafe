package com.example.android.torpedocafe;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SalesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SalesFragment extends Fragment {
    private TextView tvSalesToday;

    private SimpleDateFormat format;
    private String date;

    public SalesFragment() {
        // Required empty public constructor
    }

    public static SalesFragment newInstance() {
        return new SalesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sales, container, false);

        tvSalesToday = view.findViewById(R.id.tv_sales_today);

        format = new SimpleDateFormat("dd-MM-yyyy");
        date = format.format(new Date());

        getSalesToday();

        return view;
    }

    private void getSalesToday(){
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Sale");

        dbref.child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int sale = 0;

                for(DataSnapshot child: snapshot.getChildren()){
                    sale+= child.getValue(Integer.class);
                }

                tvSalesToday.setText(String.valueOf(sale) + " PKR");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}