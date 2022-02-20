package com.exception.swasthya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class SignUpHospital extends AppCompatActivity {

    private FirebaseAuth mAuth;


    EditText editTextName;
    EditText editTextPhone;
    EditText editTextEmail;
    EditText editTextTotalBeds;
    EditText editTextpassword;
    Button buttonFetchLocation, createHospitalAccount;
    String geoHash;
    double latitude;
    double longitude;
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_hospital);

        initUI();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        buttonFetchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
            }
        });

        createHospitalAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hospitalName = editTextName.getText().toString();
                String phoneNumber = editTextPhone.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextpassword.getText().toString();

                if (email.isEmpty()){
                    editTextEmail.setError("This is required");
                }

                if (password.isEmpty()){
                    editTextpassword.setError("This is required");
                }


                final ProgressDialog dialog = new ProgressDialog(SignUpHospital.this);
                dialog.setTitle("Please wait!");
                dialog.setMessage("While we are creating an account for you");

                createHospitalAccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.show();
                        mAuth = FirebaseAuth.getInstance();
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(SignUpHospital.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        dialog.dismiss();
                                        if (task.isSuccessful()) {
                                            if(geoHash!=null) {
                                                String UId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                //initially set all number of beds = 0
                                                Hospital hospital = new Hospital(hospitalName, longitude, latitude, geoHash, 0, 0, 0, 0, 0, 0, email, UId);
                                                FirebaseFirestore.getInstance()
                                                        .collection("Hospitals")
                                                        .document(UId)
                                                        .set(hospital)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            Toast.makeText(SignUpHospital.this, "Hospital ID created", Toast.LENGTH_LONG).show();
                                                            SharedPreferences preferences = SignUpHospital.this.getSharedPreferences("MyPref",MODE_PRIVATE);
                                                            preferences.edit().putBoolean("isHospital", true);
                                                            Intent intent = new Intent(SignUpHospital.this, ChangeNumberOfBeds.class);
                                                            startActivity(intent);
                                                            SignUpHospital.this.finish();
                                                        } else {
                                                            Toast.makeText(SignUpHospital.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                            }else{
                                                Toast.makeText(SignUpHospital.this, "Please Presss the button Fetch location",Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Toast.makeText(SignUpHospital.this, "Authentication failed. Please Retry"
                                                            + task.getException().getMessage(),
                                                    Toast.LENGTH_SHORT).show();


                                        }
                                    }
                                });
                    }
                });


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


    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            mFusedLocationClient.getLastLocation()
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("SWA", "Failed" + e.getMessage());
                        }
                    })
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            if (location != null) {
                                Log.i("SWA", "got location");
                                buttonFetchLocation.setVisibility(View.GONE);
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                geoHash = getGeoHash(latitude, longitude);
                                createHospitalAccount.setEnabled(true);
                            }
                        }
                    });
        } else {
            requestPermissions();
        }
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION},  PERMISSION_ID);
    }



    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());


    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            geoHash = getGeoHash(latitude,longitude);
        }
    };


    private String getGeoHash(double latitude, double longitude) {
        return GeoFireUtils.getGeoHashForLocation(new GeoLocation(latitude, longitude));

    }


}