package com.example.android.torpedocafe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddItemFragment extends Fragment {
    private EditText etName, etPrice, etStock;
    private Button btnAdd;
    private String name, price, stock;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);

        etName = view.findViewById(R.id.et_add_name);
        etPrice = view.findViewById(R.id.et_add_price);
        etStock = view.findViewById(R.id.et_add_stock);

        btnAdd = view.findViewById(R.id.btn_add_add);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
            }
        });

        return view;
    }

    private void addItem(){
        getData();

        if(name.isEmpty() || stock.isEmpty() || price.isEmpty()){
            Toast.makeText(getActivity().getBaseContext(), "All Fields Required!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(Double.parseDouble(price) < 0.0){
            Toast.makeText(getActivity().getBaseContext(), "Enter Valid Price!", Toast.LENGTH_SHORT).show();
            return;
        }

        Item item = new Item(name, Double.parseDouble(price), Integer.parseInt(stock));

        FirebaseController firebaseController = new FirebaseController();
        firebaseController.addNewItem(item);

        Toast.makeText(getActivity().getBaseContext(), "Item Added Successfully!", Toast.LENGTH_SHORT).show();

        clearFields();
    }

    private void clearFields(){
        etStock.setText("");
        etPrice.setText("");
        etName.setText("");
    }

    private void getData(){
        name = etName.getText().toString();
        price = etPrice.getText().toString();
        stock = etStock.getText().toString();
    }

    public AddItemFragment(){}

    public static AddItemFragment newInstance(){
        return new AddItemFragment();
    }
}