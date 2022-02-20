package com.exception.swasthya;


import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryBounds;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermissions();//checks and asks for the permissoins in runtime

    }

    public void checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.INTERNET) ==
                PackageManager.PERMISSION_GRANTED
                ) {
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser == null) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
                this.finish();
            } else {
                setContentView(R.layout.activity_main);
                startMain();
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

    }

    private void getLastLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1002);
            }
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("SWA", "Failed"+ e.getMessage());
                    }
                })
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Log.i("SWA", "succ");

                        if (location != null) {
                            fetchHospitals(location);
                        }
                    }
                });
    }

    private void fetchHospitals(Location location) {
        Toast.makeText(this, "Location Fetched", Toast.LENGTH_SHORT).show();
        Log.i("SWA", "Location");


        final GeoLocation center = new GeoLocation(location.getLatitude(), location.getLongitude());
        final double radiusInM = 50 * 1000;

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
                                double lat = doc.getDouble("lat");
                                double lng = doc.getDouble("lng");

                                // We have to filter out a few false positives due to GeoHash
                                // accuracy, but most will match
                                GeoLocation docLocation = new GeoLocation(lat, lng);
                                double distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center);
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
                double lat = documentSnapshot.getDouble("lat");
                double lng = documentSnapshot.getDouble("lng");

                GeoLocation docLocation = new GeoLocation(lat, lng);
                double distance = GeoFireUtils.getDistanceBetween(docLocation, center);

                double lat1 = t1.getDouble("lat");
                double lng1 = t1.getDouble("lng");

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
    private void showHospitals() {
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        TextView textView = findViewById(R.id.infoText);
        TextView icuBedTxt = findViewById(R.id.icuBeds);
        TextView covidBedsTxt = findViewById(R.id.covidBeds);
        TextView hospitalName = findViewById(R.id.hospitalName);

        if (hospitals.size() > 0) {
            hospitalName.setText(hospitals.get(0).getmHospitalName());
            covidBedsTxt.setText(String.valueOf(hospitals.get(0).getmNumberOfBedsVacant()));
        } else {
            textView.setText("No hope for you! No Hospitals nearby. Say good bye to your family and friends.");
        }
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

    }
}