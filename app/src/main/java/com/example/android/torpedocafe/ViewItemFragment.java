package com.example.android.torpedocafe;

import android.app.DownloadManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewItemFragment extends Fragment {
    private FirebaseRecyclerAdapter adapter;

    private RecyclerView rv;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Items");

    public ViewItemFragment() {
        // Required empty public constructor
    }
    public static ViewItemFragment newInstance() {
        return new ViewItemFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.recycler_view, container, false);

        rv = view.findViewById(R.id.rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);

        populate();

        return view;
    }

    private void populate(){
        Query query = databaseReference;

        FirebaseRecyclerOptions<Item> options = new FirebaseRecyclerOptions.Builder<Item>().setQuery(query, Item.class).build();

        adapter = new FirebaseRecyclerAdapter<Item, ViewHolder>(options){

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_items, parent, false);

                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i, @NonNull final Item item) {
                viewHolder.setData(item.getName(), String.valueOf(item.getPrice()), String.valueOf(item.getStock()));

                viewHolder.btnUpdate.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if(item.getStock() != Integer.parseInt(viewHolder.etStock.getText().toString())){
                            Controller controller = new Controller();
                            controller.addStock(item, viewHolder.etStock.getText().toString());
                        }

                        item.setPrice(Double.parseDouble(viewHolder.etPrice.getText().toString()));

                        FirebaseController firebaseController = new FirebaseController();
                        firebaseController.addNewItem(item);

                        Toast.makeText(getActivity().getBaseContext(), item.getName()+" Updated Successfully!", Toast.LENGTH_SHORT).show();
                        return true;
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

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        EditText etStock, etPrice;
        Button btnUpdate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_view_name);

            etStock = itemView.findViewById(R.id.et_view_stock);
            etPrice = itemView.findViewById(R.id.et_view_price);

            btnUpdate = itemView.findViewById(R.id.btn_view_update);
        }

        public void setData(String name, String price, String stock){
            tvName.setText(name);

            etPrice.setText(price);
            etStock.setText(stock);
        }
    }

    @Override
    public void onDestroyView(){
        rv.setAdapter(null);
        rv = null;
        databaseReference = null;

        super.onDestroyView();
    }
}