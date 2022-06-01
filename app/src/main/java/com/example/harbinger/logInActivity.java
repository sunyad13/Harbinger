package com.example.harbinger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.harbinger.Models.Users;
import com.example.harbinger.databinding.ActivityLogInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class logInActivity extends AppCompatActivity {

    ActivityLogInBinding binding;

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        getSupportActionBar().hide();
        progressDialog = new ProgressDialog(logInActivity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We Are Creating Your Account.");

        binding.btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!binding.txtUsername.getText().toString().isEmpty() && !binding.txtEmail.getText().toString().isEmpty() && !binding.txtPassword.getText().toString().isEmpty())
                {
                    progressDialog.show();
                    mAuth.createUserWithEmailAndPassword(binding.txtEmail.getText().toString(),binding.txtPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful())
                                    {
                                        Users user = new Users(binding.txtUsername.getText().toString(), binding.txtEmail.getText().toString(),binding.txtPassword.getText().toString());
                                        String id = task.getResult().getUser().getUid();
                                        database.getReference().child("Users").child(id).setValue(user);

                                        Toast.makeText(logInActivity.this,"Log In Successful",Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                      Toast.makeText(logInActivity.this, task.getException().toString(),Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
                else
                {
                    Toast.makeText(logInActivity.this,"Enter Credentials", Toast.LENGTH_SHORT).show();
                }

            }
        });

        binding.txtAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(logInActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });

    }
}