package com.chinuEvent.chinuevent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.cartViewHolder> {

    private ArrayList<CartItemIdQuant> mCartItemList;
    private OnCartQuantChangeClickListener cartDecrementClickListener;


    public interface OnCartQuantChangeClickListener {
        void onDecrementClick(int position, String Quant, String ID);
        void onIncrementClick(int position,String Quant,String ID);
    }

    public void SetOnCartItemClickListener(OnCartQuantChangeClickListener listener){
        cartDecrementClickListener = listener;
    }

    public static class cartViewHolder extends RecyclerView.ViewHolder {

        public TextView CartItemName;
        public TextView CartItemPrice;
        public TextView CartItemQuant,CartItemId;
        public TextView CartItemIncrement,CartItemDecrement;


        public cartViewHolder(@NonNull View itemView, final OnCartQuantChangeClickListener mListener) {
            super(itemView);
            CartItemName = itemView.findViewById(R.id.cartItemName);
            CartItemPrice = itemView.findViewById(R.id.cartItemPrice);
            CartItemQuant = itemView.findViewById(R.id.cartItemQuant);
            CartItemIncrement = itemView.findViewById(R.id.cartItemIncrease);
            CartItemDecrement = itemView.findViewById(R.id.cartItemDecrease);
            CartItemId = itemView.findViewById(R.id.textViewID);

            CartItemIncrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CartItemQuant.setText(Integer.toString(
                            Integer.parseInt(CartItemQuant.getText().toString())+1
                    ));
                    if ( mListener!= null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onIncrementClick(position,CartItemQuant.getText().toString(),CartItemId.getText().toString());
                        }
                    }
                }
            });

            CartItemDecrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!CartItemQuant.getText().toString().equals("0"))
                        CartItemQuant.setText(Integer.toString(
                                Integer.parseInt(CartItemQuant.getText().toString())-1
                        ));

                    if ( mListener!= null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onDecrementClick(position,CartItemQuant.getText().toString(),CartItemId.getText().toString());
                        }
                    }
                }
            });
        }
    }

    public CartAdapter(ArrayList<CartItemIdQuant> cartItemList){
        mCartItemList = cartItemList;
    }
    @NonNull
    @Override
    public CartAdapter.cartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        cartViewHolder evh = new cartViewHolder(v,cartDecrementClickListener);
        return  evh;
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.cartViewHolder holder, int position) {
        CartItemIdQuant currentCartItem = mCartItemList.get(position);
        holder.CartItemName.setText(currentCartItem.getName());
        holder.CartItemPrice.setText(currentCartItem.getPrice());
        holder.CartItemQuant.setText(currentCartItem.getQuant());
        holder.CartItemId.setText( currentCartItem.getID());
    }

    @Override
    public int getItemCount() {
        return mCartItemList.size();
    }


}
