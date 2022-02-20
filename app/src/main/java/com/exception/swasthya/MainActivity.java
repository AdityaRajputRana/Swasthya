package com.exception.swasthya;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryBounds;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private FirebaseAuth mAuth;
    Button button_number_of_beds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_number_of_beds = findViewById(R.id.update_beds_main_activity);

        button_number_of_beds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ChangeNumberOfBeds.class));
            }
        });

        checkPermissions();//checks and asks for the permissoins in runtime


    }

    public void checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.INTERNET) ==
                PackageManager.PERMISSION_GRANTED
        ) {
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                SharedPreferences preferences = this.getSharedPreferences("MyPref", MODE_PRIVATE);
                if(!preferences.contains("isHospital")) {
                    FirebaseFirestore.getInstance().collection("Hospitals")
                            .document(mAuth.getUid())
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putBoolean("isHospital", true);
                                editor.apply();
                            }else{
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putBoolean("isHospital", false);
                                editor.apply();
                            }
                        }
                    });
                }

                if (preferences.getBoolean("isHospital", true)) {
                    button_number_of_beds.setVisibility(View.VISIBLE);
                }

                    startMain();


            } else {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.INTERNET, Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
            }
        }
    }

    private FusedLocationProviderClient fusedLocationClient;

    private void startMain() {
        /*How we are going to find
       get current location - complete
       covert to geohash
       search geo hash
       display
       display on map
       diplay a slider for different hospitals
        * */

        getLastLocation();
        loadMap();
    }

    private void loadMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void getLastLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1002);
            }
            return;
        }

        fusedLocationClient.getLastLocation()
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
                            fetchHospitals(location);
                            mLocation = location;
                            addGPSMarker();
                        }
                    }
                });
    }

    private void addGPSMarker() {
        if (map != null
                && mLocation != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                map.setMyLocationEnabled(true);
            } else {
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()))
                        .title("Your Location"));
            }
            GoogleMapOptions options = new GoogleMapOptions();
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLocation.getLatitude(),
                    mLocation.getLongitude()),13));
            locateHospitalsOnMap();
        }
    }

    private void locateHospitalsOnMap() {
        if (map != null && hospitals != null){
            for (Hospital h : hospitals){
                map.addMarker(new MarkerOptions()
                .position(new LatLng(h.getmHospitalLatitude(), h.getmHospitalLongitude()))
                .title(h.getmHospitalName()));

                Log.i("Showing hospital","hospital" );
            }
        }
    }

    public void logOut(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, SignUpActivity.class));
        finish();
    }

    private Location mLocation;

    private void fetchHospitals(Location location) {
        Toast.makeText(this, "Location Fetched", Toast.LENGTH_SHORT).show();
        Log.i("SWA", "Location");


        final GeoLocation center = new GeoLocation(location.getLatitude(), location.getLongitude());
        final double radiusInM = 100 * 1000;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<GeoQueryBounds> bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInM);
        final List<Task<QuerySnapshot>> tasks = new ArrayList<>();
        for (GeoQueryBounds b : bounds) {
            Query q = db.collection("Hospitals")
                    .orderBy("mGeoHash")
                    .startAt(b.startHash)
                    .endAt(b.endHash);

            tasks.add(q.get());
        }

// Collect all the query results together into a single list
        Tasks.whenAllComplete(tasks)
                .addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Task<?>>> t) {
                        List<DocumentSnapshot> matchingDocs = new ArrayList<>();

                        for (Task<QuerySnapshot> task : tasks) {
                            QuerySnapshot snap = task.getResult();
                            for (DocumentSnapshot doc : snap.getDocuments()) {
                                matchingDocs.add(doc);
                            }
                        }

                        sortDocs(matchingDocs, center);

                    }
                });
    }

    private void sortDocs(List<DocumentSnapshot> matchingDocs, GeoLocation center) {
        Collections.sort(matchingDocs, new Comparator<DocumentSnapshot>() {
            @Override
            public int compare(DocumentSnapshot documentSnapshot, DocumentSnapshot t1) {
                double lat = documentSnapshot.getDouble("mHospitalLatitude");
                double lng = documentSnapshot.getDouble("mHospitalLongitude");

                GeoLocation docLocation = new GeoLocation(lat, lng);
                double distance = GeoFireUtils.getDistanceBetween(docLocation, center);

                double lat1 = t1.getDouble("mHospitalLatitude");
                double lng1 = t1.getDouble("mHospitalLongitude");

                // We have to filter out a few false positives due to GeoHash
                // accuracy, but most will match
                GeoLocation docLocation2 = new GeoLocation(lat1, lng1);
                double distance1 = GeoFireUtils.getDistanceBetween(docLocation2, center);

                if (distance == distance1) {
                    return 0;
                } else if (distance > distance1){
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        hospitals = new ArrayList<Hospital>();
        for (DocumentSnapshot snap: matchingDocs) {
            Hospital hospital = snap.toObject(Hospital.class);
            hospitals.add(hospital);
        }

        showHospitals();
    }

    private ProgressBar progressBar;
    private int hospitalIndex = 0;

    TextView textView;
    TextView icuBedTxt;
    TextView covidBedsTxt;
    TextView normalBedsTxt;
    TextView hospitalName;

    Button nextBtn;
    Button prevBtn;
    Button directionBtn;

    private void showHospitals() {
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        if (textView == null) {
            setUpCommands();
        }


        if (hospitals.size() > 0) {
           setInformation();
        } else {
            findViewById(R.id.hospitalDetailsLayout).setVisibility(View.GONE);
            textView.setText("No Beds found in our database 100 km radius! No Hospitals nearby.");
        }
        locateHospitalsOnMap();
    }

    private void setInformation() {
        Hospital hospital = hospitals.get(hospitalIndex);
        hospitalName.setText(hospital.getmHospitalName());
        if (hospital.getmVacantICUBeds() <= 0){
            icuBedTxt.setTextColor(Color.RED);
        } else {
            icuBedTxt.setTextColor(Color.GREEN);
        }
        String icuText = "ICU Beds : " + String.valueOf(hospital.getmVacantICUBeds())
                + "/" + String.valueOf(hospital.getmTotalICUBeds());
        icuBedTxt.setText(icuText);

        if (hospital.getmVacantCovidBeds() <= 0){
            covidBedsTxt.setTextColor(Color.RED);
        } else {
            covidBedsTxt.setTextColor(Color.GREEN);
        }
        String covidText = "Covid Beds : " + String.valueOf(hospital.getmVacantCovidBeds())
                + "/" + String.valueOf(hospital.getmTotalNoOfCovidBeds());
        covidBedsTxt.setText(covidText);

        if (hospital.getmVacantNormalBeds() <= 0){
            normalBedsTxt.setTextColor(Color.RED);
        } else {
            normalBedsTxt.setTextColor(Color.GREEN);
        }
        String normalText = "Covid Beds : " + String.valueOf(hospital.getmVacantNormalBeds())
                + "/" + String.valueOf(hospital.getmTotalNormalBeds());
        normalBedsTxt.setText(normalText);

        textView.setText(String.valueOf(hospitals.size())+ " Hospitals found in our database");

        setUpShiftButtons();
    }

    private void setUpShiftButtons() {
        if (hospitals.size() - 1 <= hospitalIndex){
            nextBtn.setEnabled(false);
        } else {
            nextBtn.setEnabled(true);
        }

        if (hospitalIndex <= 0){
            prevBtn.setEnabled(false);
        } else {
            prevBtn.setEnabled(true);
        }
    }

    private void setUpCommands() {
        textView = findViewById(R.id.infoText);
        icuBedTxt = findViewById(R.id.icuBeds);
        covidBedsTxt = findViewById(R.id.covidBeds);
        normalBedsTxt = findViewById(R.id.normalBeds);
        hospitalName = findViewById(R.id.hospitalName);

        nextBtn = findViewById(R.id.next);
        prevBtn = findViewById(R.id.prev);
        directionBtn = findViewById(R.id.direction);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hospitalIndex++;
                setInformation();
                moveCamera();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hospitalIndex--;
                setInformation();
                moveCamera();
            }
        });

        directionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = "https://www.google.com/maps/dir/?api=1&destination=" + String.valueOf(
                        hospitals.get(hospitalIndex).getmHospitalLatitude()
                ) + "," + String.valueOf(hospitals.get(hospitalIndex).getmHospitalLongitude());
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(address));
                startActivity(intent);
            }
        });
    }

    private void moveCamera() {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(hospitals.get(hospitalIndex).getmHospitalLatitude(),
                hospitals.get(hospitalIndex).getmHospitalLongitude()), 13));
    }

    private ArrayList<Hospital> hospitals;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001){
            checkPermissions();
        }

        if (requestCode == 1002){
            getLastLocation();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        addGPSMarker();
    }

    private GoogleMap map;


}