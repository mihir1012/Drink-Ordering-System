package com.chinuEvent.chinuevent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private ArrayList<CartItemIdQuant> cartItemArrayList;
    private SharedPreferences pref;
    private DatabaseReference dbReff,dbToken;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CartAdapter cartAdapter;
    private AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        System.out.println("dsdsds");
        pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        dbReff = FirebaseDatabase.getInstance().getReference().child("CartItem");
        dbToken = FirebaseDatabase.getInstance().getReference().child("TokenNumber");

        CreateList();
        BuildRecyclerView();
        CountTotal();


    }

    private void CreateList(){
        cartItemArrayList = new ArrayList<>();

        dbReff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if(!data.child("name").getValue().toString().equals("notToDisplayThisItem")) {
                        cartItemArrayList.add(new CartItemIdQuant(
                                data.child("id").getValue().toString(),
                                data.child("quant").getValue().toString(),
                                data.child("name").getValue().toString(),
                                data.child("price").getValue().toString()
                        ));
                        Log.e("THE DATA", data.toString());

                    }
                }
                Log.e("INHERE","");
                cartAdapter.notifyDataSetChanged();
                CountTotal();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException(); // never ignore possible errors
            }
        });
//        String itemname[] = pref.getString("itemIDs","").split(",");
//        for (String item : itemname){
//            Log.e("THE ITEMS",item);
//        }
    }

    private void BuildRecyclerView(){
        recyclerView = findViewById(R.id.recyclerCartView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        cartAdapter = new CartAdapter(cartItemArrayList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(cartAdapter);
        Log.e("Message","from build recyclerVIew");
        cartAdapter.SetOnCartItemClickListener(new CartAdapter.OnCartQuantChangeClickListener() {
            @Override
            public void onDecrementClick(int position, String Quant,String ID) {
                if(Quant.equals("0")){
                    cartItemArrayList.remove(position);
                    Log.e("THE POS",Integer.toString(position) );
                    System.out.println(position);
                    dbReff.child(ID).removeValue();
                    cartAdapter.notifyItemRemoved(position);
                }
                else{
                    dbReff.child(ID).child("quant").setValue(Quant);
                }
                CountTotal();
            }

            @Override
            public void onIncrementClick(int position, String Quant, String ID) {
                dbReff.child(ID).child("quant").setValue(Quant);
                CountTotal();
            }
        });
    }

    public void CancleOrder(View view){
        Log.e("The Cancle","Order Cancled");
        builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you Want to Delete Order?").setTitle("Delete Cart Items")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbReff.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot data : dataSnapshot.getChildren()){
                                    if(!data.child("name").getValue().toString().equals("notToDisplayThisItem")){
                                        dbReff.child(data.child("id").getValue().toString()).removeValue();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
//                        cartAdapter.notify();
                        finish();
                    }
                })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        CountTotal();
    }
    public void PlaceOrder(View view){
        builder = new AlertDialog.Builder(this);
        builder.setMessage("Order placed SuccessFully").setTitle("Order Placed")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TextView textView = findViewById(R.id.CartTotalView);
                        dbReff.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot data : dataSnapshot.getChildren()){
                                    if(!data.child("name").getValue().toString().equals("notToDisplayThisItem")){
                                        dbReff.child(data.child("id").getValue().toString()).removeValue();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
//                        cartAdapter.notify();
                        dbToken.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot data : dataSnapshot.getChildren()){
                                    dbToken.child("TokenNum").setValue(
                                     Integer.toString( Integer.parseInt(
                                             data.getValue().toString()
                                     )+1)
                                    );
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        finish();
                    }
                });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void CountTotal(){

        dbReff.addListenerForSingleValueEvent(new ValueEventListener() {
            int Total=0;
            TextView textView = findViewById(R.id.CartTotalView);
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    if(!data.child("name").getValue().toString().equals("notToDisplayThisItem")){
                        Total+=Integer.parseInt(data.child("quant").getValue().toString())*Integer.parseInt(data.child("price").getValue().toString());
                    }
                }

                textView.setText(Integer.toString( Total));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
