package com.exception.swasthya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null){
            Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
            startActivity(intent);
        } else {
            //This is where main activity is loaded.
            setContentView(R.layout.activity_main);

        }


    }
}