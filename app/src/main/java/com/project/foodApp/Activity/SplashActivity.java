package com.project.foodApp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.foodApp.MainActivity;
import com.project.foodApp.R;

//màn hình đầu tiên khi mở ứng dụng
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        //delay giao diện 2s
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextActivity();
            }
        },2000);
    }
    private void nextActivity() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if(user == null){
//            //chưa login
//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);
//        }else{
//            //chuyển vào màn hình activity
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//        }
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}