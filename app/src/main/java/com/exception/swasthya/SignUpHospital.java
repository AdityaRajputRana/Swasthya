package com.exception.swasthya;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignUpHospital extends AppCompatActivity {

    EditText editTextName;
    EditText editTextPhone;
    EditText editTextEmail;
    EditText editTextTotalBeds;
    EditText editTextpassword;
    Button buttonFetchLocation, createHospitalAccount;
    String geoHash;
    float latitude;
    float longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_hospital);

        initUI();


        buttonFetchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //fetch location and store in global geohash, latitude longitude

            }
        });

        createHospitalAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hospitalName = editTextName.getText().toString();
                String phoneNumber = editTextPhone.getText().toString();
                String email = editTextEmail.getText().toString();

                try{
                    int totalBeds = Integer.parseInt(editTextTotalBeds.getText().toString());
                }catch (Exception e){
                    e.printStackTrace();
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpHospital.this);
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Please enter an integer in place of number of beds");

                    alert.show();
                }

                String password = editTextpassword.getText().toString();


                //create hospital object to store in database

                Hospital hospital= new Hospital(hospitalName,longitude,latitude,geoHash,totalBeds,totalBeds,email);
            }
        });

    }

    public void initUI(){
        editTextTotalBeds = (EditText) findViewById(R.id.editText_total_beds);
        editTextPhone = (EditText) findViewById(R.id.editText_phone);
        editTextEmail = (EditText) findViewById(R.id.editText_email);
        editTextName = (EditText) findViewById(R.id.editText_name);
        editTextpassword = (EditText) findViewById(R.id.editText_pswd);
        buttonFetchLocation = findViewById(R.id.button_get_location_hospital_sign_up);
        createHospitalAccount = findViewById(R.id.button_create_hospital_account);


    }
}