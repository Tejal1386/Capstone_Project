package com.example.capstone.furniturestore.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.capstone.furniturestore.CategoryActivity;
import com.example.capstone.furniturestore.Models.Department;
import com.example.capstone.furniturestore.R;
import com.example.capstone.furniturestore.Models.Address;

import java.util.ArrayList;

/**
 * Created by mankirankaur on 2018-03-31.
 */

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    Context ctx;
    ArrayList<Address> arrayListaddress = new ArrayList<Address>();

    public AddressAdapter(ArrayList<Address> arrayListaddress, Context ctx) {
        this.arrayListaddress = (ArrayList<Address>) arrayListaddress;
        this.ctx = ctx;
    }


    @Override
    public AddressAdapter.AddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_layout, parent, false);
        AddressAdapter.AddressViewHolder addressViewHolder = new AddressAdapter.AddressViewHolder(view, ctx, arrayListaddress);
        return addressViewHolder;
    }

    @Override
    public void onBindViewHolder(AddressViewHolder holder, int position) {

        Address addressuser = arrayListaddress.get(position);
        holder.tvFullName.setText(addressuser.getUfullname());
        holder.tvAddress.setText(addressuser.getUaddress());

    }

    @Override
    public int getItemCount() {
        return arrayListaddress.size();
    }

    public static class AddressViewHolder extends RecyclerView.ViewHolder {

        public TextView tvFullName;
        public TextView tvAddress;

        ArrayList<Address> arrayListaddress = new ArrayList<Address>();
        Context ctx;
        public AddressViewHolder(View itemView,Context ctx,ArrayList<Address> arrayListaddress)
        {
            super(itemView);
            this.arrayListaddress = arrayListaddress;
            this.ctx = ctx;
            tvFullName = (TextView) itemView.findViewById(R.id.tvFullName);
            tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
        }

    }



}
