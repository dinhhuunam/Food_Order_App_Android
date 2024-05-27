package com.project.foodApp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.foodApp.MainActivity;
import com.project.foodApp.Model.User;
import com.project.foodApp.R;

//đăng ký
public class SignUpActivity extends AppCompatActivity {
    private EditText edtEmail, edtPassword, re_edtPassword,edtFullName,edtPhone;
    private TextView edtLogin;
    private Button btnSignUp;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();
        initListener();
        LoginPageTask();
    }

    private void LoginPageTask() {
        edtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initListener() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSignUp();
            }
        });
    }

    private void onClickSignUp() {
        String strFullName = edtFullName.getText().toString();
        String strEmail = edtEmail.getText().toString();
        String strPassword = edtPassword.getText().toString();
        String re_strPassword = re_edtPassword.getText().toString();
        String strPhone = edtPhone.getText().toString();

        if(re_strPassword.equals(strPassword)){
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(strEmail, strPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                if(firebaseUser!=null){
                                    String userId = firebaseUser.getUid();
                                    User user = new User(strFullName,strEmail,strPhone,strPassword);;
                                    database = FirebaseDatabase.getInstance();
                                    reference = database.getReference("Users");


                                    reference.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                //lưu thành công và gửi đối tượng user sang detail activity
//                                                Intent detailIntent = new Intent(getApplicationContext(),DetailActivity.class);
//                                                detailIntent.putExtra("user", user);
//                                                startActivity(detailIntent);
                                                // Gửi đối tượng user sang login activity
                                                Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                                                Log.d("RegisterUser","User: "+user);
                                                loginIntent.putExtra("user", user);
                                                startActivity(loginIntent);

                                                // Lưu thành công, chuyển sang LoginActivity
                                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                                startActivity(intent);
                                                finishAffinity();
                                            } else {
                                                // Lưu thất bại, thông báo cho người dùng
                                                Toast.makeText(SignUpActivity.this, "Lưu thông tin người dùng thất bại.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(SignUpActivity.this, "Đăng ký không thành công. ",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else{
            Toast.makeText(SignUpActivity.this,"Nhập lại Password",Toast.LENGTH_SHORT).show();
        }
    }

    private void init(){
        edtEmail=findViewById(R.id.edt_email);
        edtPassword=findViewById(R.id.edt_password);
        re_edtPassword=findViewById(R.id.re_edt_password);
        btnSignUp=findViewById(R.id.btn_sign_up);
        edtLogin=findViewById(R.id.loginPage);
        edtFullName=findViewById(R.id.edt_full_name);
        edtPhone=findViewById(R.id.edt_phone);
    }
}