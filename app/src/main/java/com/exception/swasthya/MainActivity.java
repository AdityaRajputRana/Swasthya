package com.exception.swasthya;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermissions();//checks and asks for the permissoins in runtime

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
            this.finish();

            //sign in and sign up using email added for user
            //sign in as hospital ui created. update hospital beds left
        } else {
            setContentView(R.layout.activity_main);
        }

    }

   public void checkPermissions(){

   }



}