package com.chinuEvent.chinuevent;

import android.util.Log;

public class CartItemIdQuant {
    private String ID;
    private String Quant;
    private String Name;
    private String Price;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getQuant() {
        return Quant;
    }

    public void setQuant(String quant) {
        Quant = quant;
    }

    public CartItemIdQuant(String NewID, String NewQuant,String NewName,String NewPrice){
        ID = NewID;
        Quant = NewQuant;
        Name = NewName;
        Price = NewPrice;
        Log.e("the" , "Method Called From Constructor");

    }

    public CartItemIdQuant(){

    }
}
