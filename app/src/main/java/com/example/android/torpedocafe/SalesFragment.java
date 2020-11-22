package com.example.android.torpedocafe;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SalesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SalesFragment extends Fragment {
    private TextView tvSalesToday, tvReport;

    private SimpleDateFormat format;
    private String date;

    private ArrayList<String> namesFromDB, namesIndividual;
    private ArrayList<Integer> quantityFromDB, quantityIndividual;

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
        tvReport = view.findViewById(R.id.tv_sales_report);

        format = new SimpleDateFormat("dd-MM-yyyy");
        date = format.format(new Date());

        getSalesToday();

        getReport();
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

        dbref = null;
    }

    private void getReport(){
        final DatabaseReference name = FirebaseDatabase.getInstance().getReference("Sold").child(date);

        namesFromDB = new ArrayList<>();
        quantityFromDB = new ArrayList<>();

        name.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int quant = 0; // get quantity here first. if 0, skip.

                for(DataSnapshot child: snapshot.getChildren()){
                    for(DataSnapshot grandChild: child.getChildren()){
                        quant = grandChild.getValue(Integer.class);
                        if (quant <= 0) continue;

                        namesFromDB.add(grandChild.getKey());
                        quantityFromDB.add(quant);
                    }
                }

                // make names list of single occurrence
                namesIndividual = new ArrayList<>();
                for(int i = 0; i < namesFromDB.size(); i++){
                    if (namesIndividual.indexOf(namesFromDB.get(i)) < 0){
                        namesIndividual.add(namesFromDB.get(i));
                    }
                }

                quantityIndividual = new ArrayList<>();
                // fill zeros
                for(int i = 0; i < namesIndividual.size(); i++)
                    quantityIndividual.add(0);

                int index = -1;

                // find names in db list, get index, add the element of that index from db quantity, to actual quantity
                try {
                    for (int i = 0; i < namesIndividual.size(); i++) {
                        for (int j = 0; j <= namesFromDB.size(); j++) {
                            index = namesFromDB.indexOf(namesIndividual.get(i));

                            if (index < 0) break;

                            quantityIndividual.set(i, quantityIndividual.get(i) + quantityFromDB.get(index));
                            namesFromDB.remove(index);
                            quantityFromDB.remove(index);
                        }
                    }
                }
                catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }

                // set view
                for(int i = 0; i < namesIndividual.size(); i++){
                    tvReport.append(namesIndividual.get(i) + ": ");
                    tvReport.append(String.valueOf(quantityIndividual.get(i)));
                    tvReport.append("\n");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}