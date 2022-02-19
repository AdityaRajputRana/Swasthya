package com.exception.swasthya;


import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermissions();//checks and asks for the permissoins in runtime

    }

   public void checkPermissions(){
       if (ContextCompat.checkSelfPermission(
               getApplicationContext(), Manifest.permission.INTERNET) ==
               PackageManager.PERMISSION_GRANTED) {
           mAuth = FirebaseAuth.getInstance();
           FirebaseUser currentUser = mAuth.getCurrentUser();
           if(currentUser == null){
               Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
               startActivity(intent);
               this.finish();
           } else {
               setContentView(R.layout.activity_main);
               startMain();
           }
       } else {
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
               requestPermissions(new String[]{Manifest.permission.INTERNET}, 1001);
           }
       }
   }

    private void startMain() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001){
            checkPermissions();
        }
    }
}