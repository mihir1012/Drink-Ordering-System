package com.chinuEvent.chinuevent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.InputQueue;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ItemBottomSheet.BottomSheetListener{

    private ArrayList<ItemClass> itemList;
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton floatingActionButton;
    private DatabaseReference dbGetDataReff,dbPutDataCart;
    private SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        floatingActionButton = findViewById(R.id.FloatAddButton);
        dbGetDataReff = FirebaseDatabase.getInstance().getReference().child("Items");
        dbPutDataCart = FirebaseDatabase.getInstance().getReference().child("CartItem");
        pref = getSharedPreferences("pref", Context.MODE_PRIVATE);

        CreateList();
        BuildRecyclerView();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CartActivity.class);
                startActivity(intent);
            }
        });

    }

    private void CreateList(){
        itemList = new ArrayList<>();
//        System.out.println("out EJ EPO WRHI>RHEIORHEO>1");
//        Log.e("the" , "Method Called");

        dbGetDataReff.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){

//                    System.out.println("THE VALUE" +data.getKey().toString());
                    itemList.add(new ItemClass(R.drawable.cocktail,
                            data.child("itemName").getValue().toString(),
                            Integer.parseInt( data.child("itemPrice").getValue().toString()),
                            data.child("itemID").getValue().toString()));
//                    if(pref.getBoolean("FirstTime",true)){
//                       SharedPreferences.Editor editor = pref.edit();
//                       editor.putString("itemNames",
//                               pref.getString("itemNames","")+
//                                       data.child("itemName").getValue().toString()+",");
//                       editor.putString("itemPrices",
//                               pref.getString("itemPrices","")+data.child("itemPrice").getValue().toString()+",");
//                       editor.putString("itemIDs",
//                               pref.getString("itemIDs","")+data.child("itemID").getValue().toString()+",");
//                       editor.putBoolean("FirstTime",false);
//                       editor.commit();
//                    }

//                    System.out.println("THE VALUE  ::"+data.child("itemID").getValue().toString());
                }
                itemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        itemAdapter.notifyDataSetChanged();
//        System.out.println("out EJ EPO WRHI>RHEIORHEO>");
        Log.e("the" , "DATA FETCHED");
    }

    private void BuildRecyclerView(){

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this,3);
        itemAdapter = new ItemAdapter(itemList);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(itemAdapter);
//        Log.e("the" , "Method Called Again");
        itemAdapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ItemBottomSheet exampleBottomSheetDialog = new ItemBottomSheet();
                exampleBottomSheetDialog.setName(
                        itemList.get(position).getItemName(),
                        itemList.get(position).getItemID(),
                        Integer.toString( itemList.get(position).getItemPrice())
                );


                exampleBottomSheetDialog.show(getSupportFragmentManager(),"itemBottomSheet");
            }
        });
    }


    @Override
    public void onSaveButtonClicked(final String textQuant, final String ID,final String Name, final String Price) {
//        String CartItems = pref.getString("CartItems","");
////        Log.e("STRING",CartItems);
//        if(CartItems.equals("")){
//            SharedPreferences.Editor editor = pref.edit();
//            editor.putString("CartItems",
//                    ID+":"+textQuant+",");
//            editor.commit();
//        }
//        else{
//            int flag = 0;
//            String newAddedItem="",newCartString="";
//            String Items[] = CartItems.split(",");
//            for(String Item : Items){
//                if(Item.substring(0,2).equals(ID)){
////                    Log.e("ITEM",Item);
//                    String itemQuant= Integer.toString(
//                      Integer.parseInt(textQuant)
//                      +  Integer.parseInt(Item.substring(3))
//                    );
//                    String newItem="";
//                    newItem+=Item.substring(0,Item.indexOf(":")+1)+itemQuant;
//                    newAddedItem = newItem;
////                    Log.e("THE",newAddedItem);
//                    flag = 1;
//                }
//            }
//            if(flag==1){
//                for(String Item : Items){
//                    if(Item.substring(0,2).equals(ID)){
//                        newCartString+=newAddedItem+",";
//                    }
//                    else{
//                        newCartString+=Item+",";
//                    }
//                }
//                SharedPreferences.Editor editor = pref.edit();
//                editor.putString("CartItems",newCartString);
//                editor.commit();
////                Log.e("NEW CART",newCartString);
//            }
//            else{
//                String AddedCartItems =pref.getString("CartItems","")
//                        +ID+":"+textQuant+",";
//                SharedPreferences.Editor editor = pref.edit();
//                editor.putString("CartItems",
//                        AddedCartItems);
//                editor.commit();
//            }
//        }

        dbPutDataCart.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int databaseFlag=0;
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    if(data.child("id").getValue().toString().equals(ID)){
//                        Log.e("THE ID",data.child("id").getValue());
                        dbPutDataCart.child(ID).child("quant").setValue(
                           Integer.toString( Integer.parseInt( data.child("quant").getValue().toString())
                           + Integer.parseInt(textQuant)
                        ));
                        databaseFlag=1;
                        break;
                    }
                }

                if(databaseFlag==0){
                    dbPutDataCart.child(ID).setValue(new CartItemIdQuant(ID,textQuant,Name,Price));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });

    }
}
