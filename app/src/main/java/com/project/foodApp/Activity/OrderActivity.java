package com.project.foodApp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.foodApp.Adapter.CartAdapter;
import com.project.foodApp.Adapter.FoodListAdapter;
import com.project.foodApp.Adapter.OrderAdapter;
import com.project.foodApp.MainActivity;
import com.project.foodApp.Model.Cart;
import com.project.foodApp.Model.Order;
import com.project.foodApp.databinding.ActivityCartBinding;
import com.project.foodApp.databinding.ActivityOrderBinding;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends BaseActivity{
    ActivityOrderBinding binding;
    private RecyclerView.Adapter orderAdapter;
    private List<Order> list;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list=new ArrayList<>();
        //lấy dữ liệu từ firebase lưu vào arrayList
        initList();
        //quay về trang home
        setBackHome();
        //gửi email
        //sendEmail();
    }

    private void setBackHome() {
        Button bt = binding.backToHomePage;
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //lấy dữ liệu từ firebase lưu vào arrayList
    private void initList() {
        DatabaseReference myRef=database.getReference("Orders");
        // Lấy UID của người dùng hiện tại
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query query=myRef.child(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Duyệt qua từng child trong node "Orders"
                    if(snapshot.exists()) {
                        for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                            Order order = orderSnapshot.getValue(Order.class);
                            Log.d("HistoryOrder", "Order: " + order);
                            list.add(orderSnapshot.getValue(Order.class));
                        }
                        if(list.size()>0){
                            binding.listOrder.setLayoutManager(new GridLayoutManager(OrderActivity.this,1));
                            orderAdapter=new OrderAdapter(list);
                            binding.listOrder.setAdapter(orderAdapter);
                        }
                    }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
