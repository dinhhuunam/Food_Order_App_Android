package com.project.foodApp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.foodApp.Activity.BaseActivity;
import com.project.foodApp.Activity.CartActivity;
import com.project.foodApp.MainActivity;
import com.project.foodApp.Model.Cart;
import com.project.foodApp.Model.Foods;
import com.project.foodApp.R;
import com.project.foodApp.databinding.ActivityCartBinding;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewholder> {
    List<Cart> list;
    Context context;

    private OnQuantityChangeListener onQuantityChangeListener;

    public interface OnQuantityChangeListener {
        void onQuantityChanged();
    }
    DatabaseReference databaseReference;

    public CartAdapter(List<Cart> list,OnQuantityChangeListener onQuantityChangeListener) {
        this.list = list;
        this.databaseReference = FirebaseDatabase.getInstance().getReference("Cart");
        this.onQuantityChangeListener = onQuantityChangeListener;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View inflate= LayoutInflater.from(context).inflate(R.layout.viewholder_cart,parent,false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        Cart cartItem = list.get(position);
        holder.title.setText(list.get(position).getProductName());
        holder.feeEachItem.setText(list.get(position).getPrice()+" VND");
        holder.num.setText(list.get(position).getQuantity()+"");
        holder.totalEachTime.setText(list.get(position).getTotal());

        Glide.with(context)
                .load(list.get(position).getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);

        //xử lý sự kiện nút tăng số lượng
        holder.plusItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(holder.num.getText().toString());
                num += 1;
                holder.num.setText(num + "");

                String str = cartItem.getPrice();

                holder.totalEachTime.setText((getTotal(str, num)) + " VND");

                // cập nhật số lượng trong firebase
                cartItem.setQuantity(num);
                // cập nhật tổng tiền trong firebase
                cartItem.setTotal(Long.toString(getTotal(str, num)));
                databaseReference.child(Integer.toString(cartItem.getId())).setValue(cartItem);

                if (onQuantityChangeListener != null) {
                    onQuantityChangeListener.onQuantityChanged();
                }
            }
        });
        //xử lý sự kiện nút giảm số lượng
        holder.minusItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(holder.num.getText().toString());

//                if (num - 1 == 0) {
//                    // xóa khỏi firebase
//                    databaseReference.child(Integer.toString(cartItem.getId())).removeValue();
//                    // Remove item from list and notify adapter
//                    list.remove(position);
//                    notifyItemRemoved(position);
//                    notifyItemRangeChanged(position, list.size());
//                }
                if(num-1>=0) {
                    num -= 1;
                    holder.num.setText(num + "");
                    String str = cartItem.getPrice();
                    holder.totalEachTime.setText((getTotal(str, num)) + " VND");

                    // cập nhật số lượng trong firebase
                    cartItem.setQuantity(num);
                    // cập nhật tổng tiền trong firebase
                    cartItem.setTotal(Long.toString(getTotal(str, num)));
                    databaseReference.child(Integer.toString(cartItem.getId())).setValue(cartItem);

                    if (onQuantityChangeListener != null) {
                        onQuantityChangeListener.onQuantityChanged();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView title,totalEachTime,feeEachItem,plusItem,minusItem,num;
        ImageView pic;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.titleTxt);
            pic=itemView.findViewById(R.id.pic);
            feeEachItem=itemView.findViewById(R.id.feeEachItem);
            plusItem=itemView.findViewById(R.id.plusCartBtn);
            minusItem=itemView.findViewById(R.id.minusCartBtn);
            totalEachTime=itemView.findViewById(R.id.totalEachItem);
            num=itemView.findViewById(R.id.numberItemTxt);
        }
    }

    private Long getTotal(String str,int num){
        //xử lý price
        String kq="";
        for(int i=0;i<str.length();i++){
            if(str.charAt(i)!=' '){
                kq+=str.charAt(i);
            }
        }
        return num * Long.parseLong(kq);
    }
}