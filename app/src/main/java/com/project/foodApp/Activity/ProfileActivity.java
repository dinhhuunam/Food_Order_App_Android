package com.project.foodApp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.project.foodApp.MainActivity;
import com.project.foodApp.R;
import com.project.foodApp.databinding.ActivityMainBinding;
import com.project.foodApp.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //sự kiện xem lịch sử mua hàng
        setOrder();
        //đăng xuất
        setVariable();
    }

    private void setVariable() {
        //thêm sự kiện cho đăng xuất
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            }
        });
    }
    private void setOrder() {
        ImageView imageOrder = binding.orderHistory;
        imageOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,OrderActivity.class);
                startActivity(intent);
            }
        });
    }
}