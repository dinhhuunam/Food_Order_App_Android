package com.project.foodApp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.foodApp.MainActivity;
import com.project.foodApp.Model.User;
import com.project.foodApp.R;

//đăng nhập
public class LoginActivity extends AppCompatActivity {
    private ConstraintLayout constraintLayout;
    private Button btnLogin;
    private EditText edtEmail,edtPassWord;
    private TextView edtRegis,forgotPassword;
    private User user;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        initListener();
        getIntentUser();
        //chức năng đăng nhập
        loginPage();
        //chức năng quên mật khẩu
        resetPass();
    }

    //chức năng quên mật khẩu
    private void resetPass() {
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ResetPassActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginPage() {
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String strEmail = edtEmail.getText().toString();
                    String strPassword = edtPassWord.getText().toString();
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();

                    if (strEmail.equals("")) {
                        Toast.makeText(LoginActivity.this, "Yêu cầu nhập đầy đủ Email", Toast.LENGTH_SHORT).show();
                    } else if(strPassword.equals("")){
                        Toast.makeText(LoginActivity.this, "Yêu cầu nhập đầy đủ Password", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        mAuth.signInWithEmailAndPassword(strEmail, strPassword)
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Gửi đối tượng user sang login activity
                                            Intent cartIntent = new Intent(getApplicationContext(), CartActivity.class);
                                            cartIntent.putExtra("user", user);
                                            startActivity(cartIntent);
                                            // Sign in success, update UI with the signed-in user's information
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finishAffinity();
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(LoginActivity.this, "Sai Email/Password",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            });
        }

    private void initListener() {
        edtRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    //nhận user
    private void getIntentUser(){
        user = (User) getIntent().getSerializableExtra("user");
        if(user != null){
            Log.d("LoginUser", "User"+user);
        } else {
            Log.d("LoginUser", "User is null");
        }
    }
    private void init(){
        constraintLayout = findViewById(R.id.layout_login);
        btnLogin = findViewById(R.id.btnLogin);
        edtEmail = findViewById(R.id.inputEmail);
        edtPassWord = findViewById(R.id.inputPassword);
        edtRegis = findViewById(R.id.registerPage);
        forgotPassword = findViewById(R.id.forgotPassword);
    }
}