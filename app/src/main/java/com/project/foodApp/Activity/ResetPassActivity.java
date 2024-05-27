package com.project.foodApp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.foodApp.MainActivity;
import com.project.foodApp.R;

public class ResetPassActivity extends AppCompatActivity {
    private EditText email;
    private Button btn;
    FirebaseAuth auth;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        initListener();

        auth = FirebaseAuth.getInstance(); // Initialize FirebaseAuth
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = email.getText().toString().trim();
                if (emailAddress.isEmpty()) {
                    Toast.makeText(ResetPassActivity.this, "Điền đầy đủ email", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPassActivity.this, "Mật khẩu gửi thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ResetPassActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                    } else {
                        Toast.makeText(ResetPassActivity.this, "Failed to send reset email", Toast.LENGTH_SHORT).show();
                    }
                    }
                });
            }
        });
    }

    private void initListener() {
        email=findViewById(R.id.emailTxt);
        btn=findViewById(R.id.changePassBtn);
    }
}