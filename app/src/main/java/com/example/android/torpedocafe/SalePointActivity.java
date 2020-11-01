package com.example.android.torpedocafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SalePointActivity extends AppCompatActivity {
    private RecyclerView rv;

    private FirebaseRecyclerAdapter adapter;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Items");

    private Order order;

    private Button btnOrder;

    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sale_point_rv);

        rv = findViewById(R.id.sp_rv);

        btnOrder = findViewById(R.id.btn_order);

        GridLayoutManager layoutManager = new GridLayoutManager(getBaseContext(), 2); // spanCount is number of columns

        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);

        order = new Order();
        controller = new Controller();

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    controller.confirmOrder(order);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        populate();
    }

    private void populate(){
        Query query = databaseReference;

        FirebaseRecyclerOptions<Item> options = new FirebaseRecyclerOptions.Builder<Item>().setQuery(query, Item.class).build();

        adapter = new FirebaseRecyclerAdapter<Item, ViewHolder>(options) {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sale_point, parent, false);

                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i, @NonNull final Item item) {
                viewHolder.setData(item.getName());

                viewHolder.btnDec.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int quantity = Integer.parseInt(viewHolder.tvAmount.getText().toString());

                        if(quantity == 0) return;

                        viewHolder.tvAmount.setText(String.valueOf(quantity-1));

                        if(!controller.updateOrder(order, item, Integer.parseInt(viewHolder.tvAmount.getText().toString())) ){
                            Toast.makeText(SalePointActivity.this, "Not Enough Stock, Item Not Added!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                viewHolder.btnInc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int quantity = Integer.parseInt(viewHolder.tvAmount.getText().toString());

                        viewHolder.tvAmount.setText(String.valueOf(quantity+1));

                        if(!controller.updateOrder(order, item, Integer.parseInt(viewHolder.tvAmount.getText().toString())) ){
                            Toast.makeText(SalePointActivity.this, "Not Enough Stock, Item Not Added!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };

        rv.setAdapter(adapter);
    }

    @Override
    public void onStop(){
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart(){
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onDestroy(){
        rv.setAdapter(null);
        rv = null;
        databaseReference = null;

        super.onDestroy();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        Button btnInc, btnDec;
        TextView tvAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_sale_name);
            tvAmount = itemView.findViewById(R.id.tv_sale_amount);
            btnInc = itemView.findViewById(R.id.btn_sale_inc);
            btnDec = itemView.findViewById(R.id.btn_sale_dec);
        }

        public void setData(String name){
            tvName.setText(name);

            tvAmount.setText("0");
        }
    }
}