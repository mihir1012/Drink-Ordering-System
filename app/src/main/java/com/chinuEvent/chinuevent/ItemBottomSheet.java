package com.chinuEvent.chinuevent;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ItemBottomSheet extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    String nameView="";
    String itemID="";
    String itemPrice="";
    public void setName(String name,String ID,String price){
        nameView = name;
        itemID = ID;
        itemPrice = price;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.bottom_sheet_layout,container,false);
        Button button1= v.findViewById( R.id.ButtonBottom1);
        Button button2= v.findViewById(R.id.ButtonBottom2);
        ImageView imgView1 = v.findViewById(R.id.upButton);
        ImageView imgView2 = v.findViewById(R.id.downButton);
        final EditText editTextQuant = v.findViewById(R.id.EditNumber);
//        editTextQuant.setClickable(false);
        TextView textView = v.findViewById(R.id.ItemName);
        TextView priceView = v.findViewById(R.id.ItemPrice);
        textView.setText(nameView);
        priceView.setText(itemPrice);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onSaveButtonClicked(editTextQuant.getText().toString(),itemID,nameView,itemPrice);
                dismiss();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        imgView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextQuant.setText(Integer.toString(
                    Integer.parseInt(editTextQuant.getText().toString())+1
                ));
            }
        });

        imgView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editTextQuant.getText().toString().equals("1"))
                editTextQuant.setText(Integer.toString(
                        Integer.parseInt(editTextQuant.getText().toString())-1
                ));
            }
        });
        return v;
    }

    public interface BottomSheetListener{
        void onSaveButtonClicked(String text1,String PosID,String Name,String Price);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (BottomSheetListener) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString()+ " must implement BottomSheetListener");
        }

    }

}
