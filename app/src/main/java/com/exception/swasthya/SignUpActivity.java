package com.exception.swasthya;

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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    EditText editTextName;
    EditText editTextPhone;
    EditText editTextEmail;
    EditText editTextAadhar;
    EditText editTextpassword;
    Button button;
    TextView textViewSignUpAsHospital;
    TextView textViewSignInInstead;
    TextView textViewSignInAsHospitalInstead;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initUI();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = editTextEmail.getText().toString();
                String password = editTextpassword.getText().toString();
                mAuth = FirebaseAuth.getInstance();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(SignUpActivity.this, "Sign up Successful",
                                            Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    //Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(SignUpActivity.this, "Authentication failed. Please Retry",
                                            Toast.LENGTH_SHORT).show();


                                }
                            }
                        });

            }
        });

        textViewSignUpAsHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, SignUpHospital.class);
                startActivity(intent);
            }
        });

        textViewSignInInstead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        textViewSignInAsHospitalInstead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, SignInHospital.class);
                startActivity(intent);
            }
        });

    }

    public void initUI() {
        editTextAadhar = (EditText) findViewById(R.id.editText_user_aadhar);
        editTextPhone = (EditText) findViewById(R.id.editText_user_phone);
        editTextEmail = (EditText) findViewById(R.id.editText_user_email);
        editTextName = (EditText) findViewById(R.id.editText_user_name);
        editTextpassword = (EditText) findViewById(R.id.editText_user_pswd);
        button = (Button) findViewById(R.id.button_create_account);
        textViewSignUpAsHospital = (TextView)findViewById(R.id.text_view_sign_up_as_hospital_on_sign_up);
        textViewSignInInstead = (TextView)findViewById(R.id.text_view_sign_in_as_user_on_sign_up);
        textViewSignInAsHospitalInstead = (TextView) findViewById(R.id.textview_sign_in_as_hospital_onsignup);






    }
}