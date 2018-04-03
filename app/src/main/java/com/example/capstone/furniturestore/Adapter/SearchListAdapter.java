package com.example.capstone.furniturestore.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.capstone.furniturestore.Models.SearchItems;
import com.example.capstone.furniturestore.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tejalpatel on 2018-03-13.
 */

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.MyViewHolder> {

    ArrayList<SearchItems> arrayList = new ArrayList<>();


    public SearchListAdapter(ArrayList<SearchItems> arrayList){
        this.arrayList = arrayList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.product_Name.setText(arrayList.get(position).getproduct_Name());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView product_Name;
        public MyViewHolder(View itemView) {
            super(itemView);

            product_Name = (TextView) itemView.findViewById(R.id.txtCategoryName);
        }
    }
}
