package com.project.foodApp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.internal.service.Common;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.foodApp.Adapter.CartAdapter;
import com.project.foodApp.Adapter.FoodListAdapter;
import com.project.foodApp.MainActivity;
import com.project.foodApp.Model.Cart;
import com.project.foodApp.Model.Foods;
import com.project.foodApp.Model.Order;
import com.project.foodApp.Model.User;
import com.project.foodApp.R;
import com.project.foodApp.databinding.ActivityCartBinding;
import com.project.foodApp.databinding.ActivityListFoodsBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CartActivity extends BaseActivity implements CartAdapter.OnQuantityChangeListener{
    ActivityCartBinding binding;
    private CartAdapter cartAdapter;
    long totalAmout = 0;
    private List<Cart> list;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        list=new ArrayList<>();
        //xử lý dữ liệu nút back
        getIntenExtra();
        //lấy dữ liệu từ firebase lưu vào arrayList và tính tổng tiền
        initList();
        //xử lý sự kiện order
        processOrder();
    }

    private void processOrder() {
        binding.orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CartActivity.this);
        alertDialog.setTitle("One mor step!");
        alertDialog.setMessage("Enter your address: ");

        final EditText edtAddress = new EditText(CartActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        edtAddress.setLayoutParams(lp);
        alertDialog.setView(edtAddress); //add edit text to alert dialog
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Biến tổng tiền
                String total = String.valueOf(totalAmout);

                // Lấy thông tin người dùng từ FirebaseAuth
                FirebaseUser userInfo = FirebaseAuth.getInstance().getCurrentUser();
                String userId = userInfo.getUid();

                DatabaseReference usersRef = database.getReference("Users").child(userId);
                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String phone = dataSnapshot.child("phone").getValue(String.class);
                            String name = dataSnapshot.child("name").getValue(String.class);
                            Log.d("PhoneCart ","Phone: "+phone);
                            Log.d("NameCart ","Name: "+name);

                            // Lấy địa chỉ từ EditText
                            String address = edtAddress.getText().toString();
                            // Lấy thời gian hiện tại với định dạng ngày/tháng/năm
                            //String currentTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
                            String currentTime = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                            // Tạo một order mới
                            Order order = new Order(phone, name, address, total, currentTime);

                            // Lưu Order vào Firebase
                            DatabaseReference orderCountRef = FirebaseDatabase.getInstance().getReference("Orders").child(userId);
                            orderCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    long orderCount = dataSnapshot.getChildrenCount(); // Số đơn hàng đã có trong firebase
                                    DatabaseReference newOrderRef = FirebaseDatabase.getInstance().getReference("Orders").child(userId).child(String.valueOf(orderCount + 1));
                                    newOrderRef.setValue(order);
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });

                            // Thông báo đặt hàng thành công
                            Toast.makeText(CartActivity.this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            Log.e("FirebaseError", "Không tồn tại Uses");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Xử lý nếu có lỗi xảy ra
                        Log.e("FirebaseError", "Error fetching user data: " + databaseError.getMessage());
                    }
                });
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    //xử lý sự kiện nút Back
    private void getIntenExtra() {
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //Lấy dữ liệu từ Cart firebase theo userid của người dùng và lưu vào List
    private void initList() {
        DatabaseReference myRef=database.getReference("Cart");
        // Lấy UID của người dùng hiện tại
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Query query=myRef.orderByChild("userId").equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot issue : snapshot.getChildren()){
                        list.add(issue.getValue(Cart.class));
                    }
                    if(list.size()>0){
                        updateTotalPrice();
                        binding.cardView.setLayoutManager(new GridLayoutManager(CartActivity.this,1));
                        cartAdapter=new CartAdapter(list,CartActivity.this);
                        binding.cardView.setAdapter(cartAdapter);
//                        updateTotalPrice();
                    }
                    else{
                        long total=0;
                        binding.totalTaxTxt.setText(total+" VND");
                        binding.deliveryTxt.setText(total+" VND");
                        binding.totalTxt.setText(total+" VND");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void updateTotalPrice() {
        // Calculate total price and log details (tính tổng tiền trong cart)
        long total = 0;
        for (Cart item : list) {
            if(Long.parseLong(item.getTotal())==0){
                // xóa khỏi firebase
                FirebaseDatabase.getInstance().getReference("Cart").child(Integer.toString(item.getId())).removeValue();
                // Remove item from list and notify adapter
                list.remove(item);
            }
            total += Long.parseLong(item.getTotal());
            Log.d("Detail", "Cart: " + item);
        }
        binding.totalTaxTxt.setText(total+" VND");
        binding.deliveryTxt.setText("30 000 VND");
        total+=30000;
        totalAmout=total;
        binding.totalTxt.setText(total+" VND");
    }

    @Override
    public void onQuantityChanged() {
        updateTotalPrice();
    }
}