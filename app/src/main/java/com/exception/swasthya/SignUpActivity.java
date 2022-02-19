package com.exception.swasthya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    EditText editTextName;
    EditText editTextPhone;
    EditText editTextEmail;
    EditText editTextAadhar;
    EditText editTextpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initUI();
        setUpListeners();
    }

    private void setUpListeners() {
        findViewById(R.id.btnHospital)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SignUpActivity.this, SignUpHospital.class);
                        startActivity(intent);
                        SignUpActivity.this.finish();
                    }
                });

        findViewById(R.id.btnEmergency)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        createAnonymousUser();
                    }
                });
    }

    private void createAnonymousUser() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInAnonymously()
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                            SignUpActivity.this.finish();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Some error occurred: "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void initUI(){
        editTextEmail = (EditText) findViewById(R.id.editText_user_email);
        editTextName = (EditText) findViewById(R.id.editText_user_name);
        editTextpassword = (EditText) findViewById(R.id.editText_user_pswd);

    }
}