package com.project.foodApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.foodApp.Activity.BaseActivity;
import com.project.foodApp.Activity.CartActivity;
import com.project.foodApp.Activity.ListFoodsActivity;
import com.project.foodApp.Activity.LoginActivity;
import com.project.foodApp.Activity.ProfileActivity;
import com.project.foodApp.Adapter.BestFoodAdapter;
import com.project.foodApp.Adapter.CategoryAdapter;
import com.project.foodApp.Model.Category;
import com.project.foodApp.Model.Foods;
import com.project.foodApp.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //lấy dữ liệu từ firebase theo bestfood
        initBestFood();
        //lấy dữ liệu từ firebase theo category
        initCategory();
        //sự kiện đăng xuất và sự kiện tìm kiếm
        setVariable();
        //sự kiện giỏ hàng
        setCart();
        //lắng nghe sự kiện Profile
        setProfile();
    }

    //sự kiện profile
    private void setProfile() {
        ImageView imageViewProfile = binding.imageViewProfile;
        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để chuyển sang ProfileActivity
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    //sự kiện giỏ hàng
    private void setCart() {
        binding.cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent=new Intent(getApplicationContext(), CartActivity.class);
                 startActivity(intent);
            }
        });
    }

    private void setVariable() {
        //thêm sự kiện cho đăng xuất
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
        //sự kiện tìm kiếm
        binding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=binding.searchEdt.getText().toString();
                if(!text.isEmpty()){
                    //sau khi nhập từ khóa tìm kiếm thì gửi intent nội dung tìm kiếm sang listfood
                    Intent intent=new Intent(MainActivity.this, ListFoodsActivity.class);
                    //gửi intente text
                    intent.putExtra("text",text);
                    //gửi intent issearch
                    intent.putExtra("isSearch",true);
                    startActivity(intent);
                }
            }
        });
    }
    //All Best Food
    private void initBestFood(){
        DatabaseReference myRef = database.getReference("Foods");
        binding.progressBarBestFood.setVisibility(View.VISIBLE);
        ArrayList<Foods> list=new ArrayList<>();
        Query query=myRef.orderByChild("BestFood").equalTo(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot issue: snapshot.getChildren()){
                        list.add(issue.getValue(Foods.class));
                    }
                    if(list.size()>0){
                        binding.bestFoodView.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false));
                        RecyclerView.Adapter adapter=new BestFoodAdapter(list);
                        binding.bestFoodView.setAdapter(adapter);
                    }
                    binding.progressBarBestFood.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    //All Category dinhhuunam.job@gmail.com
    private void initCategory(){
        DatabaseReference myRef = database.getReference("Category");
        binding.progressBarCategory.setVisibility(View.VISIBLE);
        ArrayList<Category> list=new ArrayList<>();
//        Query query=myRef.orderByChild("BestFood").equalTo(true);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot issue: snapshot.getChildren()){
                        Category category = issue.getValue(Category.class);
                        //check xem có dữ liệu lấy từ firebase không
                        if(category.getName() == null){
                            Log.d("FirebaseData", "Category is null for some reason");
                        }else {
                            Log.d("FirebaseData", "Category: " + category.getName());
                        }
                        list.add(issue.getValue(Category.class));
                    }
                    if(list.size()>0){
                        binding.categoryView.setLayoutManager(new GridLayoutManager(MainActivity.this,4));
                        RecyclerView.Adapter adapter=new CategoryAdapter(list);
                        binding.categoryView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                    binding.progressBarCategory.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,"get List Category failed",Toast.LENGTH_SHORT).show();
            }
        });
    }
}