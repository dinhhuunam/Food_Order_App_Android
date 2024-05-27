package com.project.foodApp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.project.foodApp.Model.Cart;
import com.project.foodApp.Model.Order;
import com.project.foodApp.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.viewholder>{
    List<Order> list;
    Context context;

    public OrderAdapter(List<Order> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View inflate= LayoutInflater.from(context).inflate(R.layout.viewholder_order,parent,false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        holder.buyerName.setText(list.get(position).getName());
        holder.address.setText(list.get(position).getAddress());
        holder.date.setText(list.get(position).getDate());
        holder.total.setText(list.get(position).getTotal()+" VND");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView buyerName,address,date,total;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            buyerName=itemView.findViewById(R.id.buyerNameTxt);
            address=itemView.findViewById(R.id.addressTxt);
            date=itemView.findViewById(R.id.dateTxt);
            total=itemView.findViewById(R.id.totalAmountTxt);
        }
    }
}