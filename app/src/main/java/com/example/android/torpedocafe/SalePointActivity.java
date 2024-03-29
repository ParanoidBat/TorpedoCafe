package com.example.android.torpedocafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.IOException;

public class SalePointActivity extends AppCompatActivity implements ConfirmDialogFragment.DialogListener{
    private RecyclerView rv;

    private FirebaseRecyclerAdapter adapter;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Items");

    private Order order;

    private Controller controller;

    private int bill = -1;

    private String stringOrder = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sale_point_rv);

        rv = findViewById(R.id.sp_rv);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GridLayoutManager layoutManager = new GridLayoutManager(getBaseContext(), 2); // spanCount is number of columns

        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);

        order = new Order();
        controller = new Controller();

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
                            viewHolder.tvAmount.setText("0");
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
                            viewHolder.tvAmount.setText("0");
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

    @Override
    public void onBackPressed(){}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_logout:
                finish();
                return true;

            case R.id.item_order:
                try {
                    bill = controller.calculateBill(order);
                    if(bill <= 0){
                        throw new IOException("bill less than 0");
                    }

                    stringOrder = controller.orderToString(order);
                    showDialog();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialogFragment) {
        controller.confirmOrder(order);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialogFragment) {

    }

    public void showDialog(){
        DialogFragment dialogFragment = new ConfirmDialogFragment();

        Bundle args = new Bundle();
        args.putString("order", stringOrder);
        args.putInt("bill", bill);
        dialogFragment.setArguments(args);

        dialogFragment.show(getSupportFragmentManager(), "ConfirmDialogFragment");
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