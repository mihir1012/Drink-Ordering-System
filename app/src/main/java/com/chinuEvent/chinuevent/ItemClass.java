package com.chinuEvent.chinuevent;

import android.util.Log;

public class ItemClass {
    private int imageResource;
    private String itemName;
    private int itemPrice;

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    private String itemID;

    public ItemClass(int imageResource,String itemName,int itemPrice, String itemID){
        this.imageResource = imageResource;
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
//        Log.e("the" , "Method Called From Constructor");
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrize) {
        this.itemPrice = itemPrize;
    }
}
