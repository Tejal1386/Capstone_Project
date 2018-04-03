package com.example.capstone.furniturestore.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.capstone.furniturestore.Cart;
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
    public ImageView img_cart_count;
    public ImageView cart_image;

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
        img_cart_count = (ImageView) itemView.findViewById(R.id.cart_item_count);
        cart_image = (ImageView) itemView.findViewById(R.id.cart_image);
    }

    @Override
    public void onClick(View view) {

    }
}


public class CartAdapter extends RecyclerView.Adapter<CartViewHolder>{


    private List<Product> listData = new ArrayList<>();
    private Context context;

    public CartAdapter(List<Product> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_layout,parent,false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder,final int position) {
        Picasso.with(context)
                .load(listData.get(position).getProductImage())
                .resize(70,70)
                .centerCrop()
                .into(holder.cart_image);

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(""+ listData.get(position).getProductQunt(), Color.RED);
        holder.img_cart_count.setImageDrawable(drawable);
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
}
