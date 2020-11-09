package com.example.android.torpedocafe;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

public class ConfirmDialogFragment extends DialogFragment {
    private TextView tvOrder, tvBill;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_confirm, null);
        tvOrder = view.findViewById(R.id.tv_dialog_confirm_order);
        tvBill = view.findViewById(R.id.tv_dialog_confirm_bill);

        savedInstanceState = getArguments();
        setOrder(savedInstanceState.getString("order"), savedInstanceState.getInt("bill"));

        builder.setView(view)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onDialogPositiveClick(ConfirmDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onDialogNegativeClick(ConfirmDialogFragment.this);
                    }
                });

        return builder.create();
    }

    public interface DialogListener{
        public void onDialogPositiveClick(DialogFragment dialogFragment);
        public void onDialogNegativeClick(DialogFragment dialogFragment);
    }

    DialogListener listener; // instance used to deliver action events

    // method to instantiate the interface
    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        // verify that host activity implements the callback interface
        try{
            // instantiate interface to send events
            listener = (DialogListener) context;
        }
        catch (ClassCastException e){
            // The activity doesn't implement the interface
             throw new ClassCastException(getActivity().toString() + "must implement DialogListener");
        }
    }

    private void setOrder(String order, int bill){
        tvOrder.setText(order);
        tvBill.setText("Total: " + String.valueOf(bill));
    }
}