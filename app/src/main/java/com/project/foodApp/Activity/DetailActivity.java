package com.project.foodApp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.foodApp.MainActivity;
import com.project.foodApp.Model.Foods;
import com.project.foodApp.Model.Cart;
import com.project.foodApp.Model.User;
import com.project.foodApp.R;
import com.project.foodApp.databinding.ActivityDetailBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DetailActivity extends BaseActivity {
    ActivityDetailBinding binding;
    private Foods object;

    private User user;
    private int num=1;
    int maxid=0;
    DatabaseReference mDatabase;
    private List<Cart> list_cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //nhận user từ sign up
        //getIntentUser();

        //lấy chi tiết thông tin 1 sản phẩm
        getIntentExtra();

        mDatabase=database.getReference().child("Cart");
        list_cart=new ArrayList<>();

        setVariable();
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Glide.with(DetailActivity.this)
                .load(object.getImagePath())
                .into(binding.pic);
        binding.priceTxt.setText(object.getPrice() + " VND");
        binding.titleTxt.setText(object.getTitle());
        binding.descriptionTxt.setText(object.getDescription());
        binding.rateTxt.setText(object.getStar() + " Rating");
        binding.ratingBar.setRating((float) object.getStar());
        //lấy ra price của food dạng string
        String str = object.getPrice();
        binding.totalTxt.setText(getTotal(str, num) + " VND");

        //Thực hiện cộng tăng số lượng khi bấm nút +
        binding.plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num += 1;
                binding.numTxt.setText(num + " ");
                String str = object.getPrice();
                binding.totalTxt.setText((getTotal(str, num)) + " VND");
            }
        });

        //Thực hiện cộng giảm số lượng khi bấm nút -
        binding.minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num > 1) {
                    num -= 1;
                    binding.numTxt.setText(num + "");
                    String str = object.getPrice();
                    binding.totalTxt.setText((getTotal(str, num)) + " VND");
                }
            }
        });

        //Chức năng add to Card
        //khi bấm nút add to card làm 2 nhiệm vụ
        //1.lưu object foods detail vào cơ sở dữ liệu: dùng firebase
        //2.lấy từ csdl và set vào cart
        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            maxid = (int) snapshot.getChildrenCount();
                        } else {
                            maxid = 0;
                        }
                        // Xử lý total
                        String price = object.getPrice();
                        // tổng tiền
                        long total = getTotal(price, num);
                        // Lấy UID của người dùng hiện tại
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        Cart cart = new Cart(object.getId(), object.getTitle(), num, object.getPrice(),
                                Long.toString(total), object.getImagePath(),userId);
                        cart.setId(maxid);

                        Query query = mDatabase.orderByChild("productId").equalTo(cart.getProductId());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    boolean flag = false;
                                    for (DataSnapshot issue : snapshot.getChildren()) {
                                        Cart tempCart = issue.getValue(Cart.class);
                                        if (tempCart != null && tempCart.getProductId()==(cart.getProductId())) {
                                            flag = true;
                                            int newQuantity = tempCart.getQuantity() + cart.getQuantity();
                                            long newTotal = getTotal(price, newQuantity);
                                            tempCart.setUserId(userId);
                                            tempCart.setQuantity(newQuantity);
                                            tempCart.setTotal(Long.toString(newTotal));
                                            mDatabase.child(issue.getKey()).setValue(tempCart);
                                            break;
                                        }
                                    }
                                    if (!flag) {
                                        mDatabase.child(Integer.toString(maxid)).setValue(cart);
                                    }
                                } else {
                                    mDatabase.child(Integer.toString(maxid)).setValue(cart);
                                }

                                Toast.makeText(getApplicationContext(), "Data insert successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getApplicationContext(), "Get data failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Get data failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    //nhận user
    private void getIntentUser(){
        user = (User) getIntent().getSerializableExtra("user");
    }

    //nhận chi tiết foods
    private void getIntentExtra() {
        object= (Foods) getIntent().getSerializableExtra("object");
    }

    //xử lý Price dạng string
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