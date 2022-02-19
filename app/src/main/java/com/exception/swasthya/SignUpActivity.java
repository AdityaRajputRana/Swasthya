package com.exception.swasthya;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

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
    }

    public void initUI(){
        editTextAadhar = (EditText) findViewById(R.id.editText_user_aadhar);
        editTextPhone = (EditText) findViewById(R.id.editText_user_phone);
        editTextEmail = (EditText) findViewById(R.id.editText_user_email);
        editTextName = (EditText) findViewById(R.id.editText_user_name);
        editTextpassword = (EditText) findViewById(R.id.editText_user_pswd);

    }
}