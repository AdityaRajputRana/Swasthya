package com.exception.swasthya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
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
    String latitude;
    String longitude;
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
                //fetch location and store in global geohash, latitude longitude

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
                                            if(geoHash!=null) {
                                                String UId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                //initially set all number of beds = 0
                                                Hospital hospital = new Hospital(hospitalName, latitude, longitude, geoHash, 0, 0, 0, 0, 0, 0, email, UId);
                                                FirebaseFirestore.getInstance()
                                                        .collection("Hospitals")
                                                        .document(UId)
                                                        .set(hospital);

                                                Toast.makeText(SignUpHospital.this, "Hospital ID created", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(SignUpHospital.this, ChangeNumberOfBeds.class);
                                                startActivity(intent);
                                                SignUpHospital.this.finish();
                                            }else{
                                                Toast.makeText(SignUpHospital.this, "Please Presss the button Fetch location",Toast.LENGTH_LONG).show();
                                            }
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

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            latitude = location.getLatitude() +"";
                            longitude =  location.getLongitude() +"";
                            geoHash = getGeoHash(latitude,longitude);
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
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
            latitude = mLastLocation.getLatitude() + "";
            longitude =  mLastLocation.getLongitude() + "";
            geoHash = getGeoHash(latitude,longitude);
        }
    };


    private String getGeoHash(String latitude, String longitude) {
        return GeoFireUtils.getGeoHashForLocation(new GeoLocation(Float.parseFloat(latitude), Float.parseFloat(longitude)));

    }


}