package com.abulkalam.authenticatorapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {
    EditText mFullName, mEmail, mPassword, mPhone;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth auth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //connecting variables to xml file
        mFullName    = findViewById(R.id.fullName);
        mEmail       = findViewById(R.id.email);
        mPassword    = findViewById(R.id.password);
        mPhone       = findViewById(R.id.phone);
        mRegisterBtn = findViewById(R.id.registerBtn);
        mLoginBtn    = findViewById(R.id.createText);
//
       // auth  = FirebaseAuth.getInstance();
        auth=FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressBar);


//        //If the user has an account already
//        if (auth.getCurrentUser() != null){
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//            finish();
//        }
//
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email  = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Enter your Email");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Enter Password");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                //Registering the user in Firebase
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            //Part-4 Sent verification link
                            FirebaseUser fuser = auth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(Register.this, "Verification email has been sent.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG,"On Failure email not sent " + e.getMessage());
                                }
                            });


                            Toast.makeText(Register.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else{
                            Toast.makeText(Register.this, "Some Error has Occur" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });


    }
}