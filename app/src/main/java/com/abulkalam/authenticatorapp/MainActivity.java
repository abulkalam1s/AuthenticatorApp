package com.abulkalam.authenticatorapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

//import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    TextView verifyMsg;
    Button resendCode;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyMsg = findViewById(R.id.verifyMsg);
        resendCode = findViewById(R.id.resendCode);


        //#4 Verify Email
        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();
        FirebaseUser user = fAuth.getCurrentUser();
        if (!user.isEmailVerified()){
            verifyMsg.setVisibility(View.VISIBLE);
            resendCode.setVisibility(View.VISIBLE);

            resendCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(v.getContext(), "Verification email has been sent.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("tag","On Failure email not sent " + e.getMessage());
                        }
                    });
                }
            });
        }
    }
  //#4


//    public  void logout(View view){
//        FirebaseAuth.getInstance().signOut(); //LOGOUT
//        startActivity(new Intent(getApplicationContext(), Login.class)); //User is send to Login activity
//        finish();
//    }
}