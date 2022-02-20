package com.exception.swasthya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpHospital extends AppCompatActivity {

    private FirebaseAuth mAuth;


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
                String password = editTextpassword.getText().toString();

                createHospitalAccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAuth = FirebaseAuth.getInstance();
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(SignUpHospital.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            //add in firebase
                                            String UId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                            //initially set all number of beds = 0
                                            Hospital hospital = new Hospital(hospitalName, latitude,longitude,geoHash,0,0,0,0,0,0,email,UId);
                                            FirebaseFirestore.getInstance()
                                                    .collection("Hospitals")
                                                    .document(UId)
                                                    .set(hospital);

                                            Toast.makeText(SignUpHospital.this,"Hospital ID created",Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(SignUpHospital.this, ChangeNumberOfBeds.class);
                                            startActivity(intent);
                                            SignUpHospital.this.finish();
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                                            Toast.makeText(SignUpHospital.this, "Authentication failed. Please Retry"
                                                            + task.getException().getMessage(),
                                                    Toast.LENGTH_SHORT).show();


                                        }
                                    }
                                });
                    }
                });



                //String password = editTextpassword.getText().toString();

                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                String UId = firebaseFirestore.collection("Hospitals").getId();

                //Hospital hospital= new Hospital(hospitalName,longitude,latitude,geoHash,totalBeds,totalBeds,email, UId);
                //create hospital object to store in database


//                firebaseFirestore.collection("Hospitals")
//                        .document("UId")
//                        .set(hospital).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//
//                    }
//                });

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