package com.example.capstone.furniturestore.ViewHolder;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.capstone.furniturestore.Cart;
import com.example.capstone.furniturestore.Database.Database;
import com.example.capstone.furniturestore.Models.Product;
import com.example.capstone.furniturestore.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by amandeepsekhon on 2018-03-28.
 */


class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txt_cart_name,txt_price;
    public ElegantNumberButton btn_quantity;
    public ImageView cart_image;
    public ImageView delete_item;

    private ProductViewHolder.ItemClickListener itemClickListener;

    public TextView getTxt_cart_name() {
        return txt_cart_name;
    }

    public void setTxt_cart_name(TextView txt_cart_name) {
        this.txt_cart_name = txt_cart_name;
    }

    public CartViewHolder(View itemView) {
        super(itemView);
        txt_cart_name = (TextView) itemView.findViewById(R.id.cart_item_name);
        txt_price = (TextView) itemView.findViewById(R.id.cart_item_price);
        btn_quantity = (ElegantNumberButton) itemView.findViewById(R.id.btn_quantity);
        cart_image = (ImageView) itemView.findViewById(R.id.cart_image);
        delete_item = (ImageView) itemView.findViewById(R.id.delete_item);
    }

    @Override
    public void onClick(View view) {

    }
}


public class CartAdapter extends RecyclerView.Adapter<CartViewHolder>{


    private List<Product> listData = new ArrayList<>();
    private Cart cart;

    public CartAdapter(List<Product> listData, Cart cart) {
        this.listData = listData;
        this.cart = cart;
        checkList(listData);
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(cart);
        View itemView = inflater.inflate(R.layout.cart_layout,parent,false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder,final int position) {
        Picasso.with(cart)
                .load(listData.get(position).getProductImage())
                .resize(70,70)
                .centerCrop()
                .into(holder.cart_image);

      /*  TextDrawable drawable = TextDrawable.builder()
                .buildRound(""+ listData.get(position).getProductQunt(), Color.RED);
        holder.img_cart_count.setImageDrawable(drawable);*/
        holder.delete_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product order = listData.get(position);
                new Database(cart).DeleteRecord(order);
                listData.remove(position);
                notifyItemRemoved(position);
                checkList(listData);
            }
        });
holder.btn_quantity.setNumber(listData.get(position).getProductQunt());
      holder.btn_quantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
          @Override
          public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
              Product order = listData.get(position);
              order.setProductQunt(String.valueOf(newValue));
              new Database(cart).updateCart(order);


float total = 0;
List<Product> orders = new Database(cart).getCarts();
for(Product item :orders)
    total +=(Float.parseFloat(item.getProductPricenew()))*(Integer.parseInt(item.getProductQunt()));
cart.txtTotalPrice.setText("$"+total+"");


          }

      });
        holder.txt_price.setText(listData.get(position).getProductPricenew());

      //  Locale locale = new Locale("en","US");
      //  NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
      //  int price = (Integer.parseInt(String.valueOf(listData.get(position).getProductPrice()))) * (Integer.parseInt(listData.get(position).getProductQunt())) ;
       // holder.txt_price.setText(fmt.format(price));
        holder.txt_cart_name.setText(listData.get(position).getProductName());


    }



    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void checkList(List<Product> listDa){
        if(listDa.size()==0){
            Toast.makeText(cart,"Cart is Empty",Toast.LENGTH_SHORT).show();
        }
    }
}
