package com.exception.swasthya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ChangeNumberOfBeds extends AppCompatActivity {

    EditText editTextNormalTotal;
    EditText editTextNormalVacant;
    EditText editTextICUTotal;
    EditText editTextICUVacant;
    EditText editTextCovidVacant;
    EditText editTextCovidTotal;
    Hospital hospital;
    String UId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_number_of_beds);


        initUI();
        UId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance()
                .collection("Hospitals")
                .document(UId)//document ki id is uid
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    hospital = task.getResult().toObject(Hospital.class);
                    editTextCovidTotal.setText(hospital.getmTotalNoOfCovidBeds());
                    editTextCovidVacant.setText(hospital.getmVacantCovidBeds());
                    editTextNormalTotal.setText(hospital.getmTotalNormalBeds());
                    editTextNormalVacant.setText(hospital.getmVacantNormalBeds());
                    editTextICUVacant.setText(hospital.getmVacantICUBeds());
                    editTextICUTotal.setText(hospital.getmTotalICUBeds());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChangeNumberOfBeds.this, "Failed fetching total beds", Toast.LENGTH_SHORT).show();
            }
        });





        Button button = findViewById(R.id.button_update_beds);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(!editTextCovidTotal.getText().toString().isEmpty()){
                    hospital.setmTotalNoOfCovidBeds(Integer.parseInt(editTextCovidTotal.getText().toString()));
                }
                if(!editTextCovidVacant.getText().toString().isEmpty()){
                    hospital.setmTotalNoOfCovidBeds(Integer.parseInt(editTextCovidVacant.getText().toString()));
                }
                if(!editTextICUTotal.getText().toString().isEmpty()){
                    hospital.setmTotalNoOfCovidBeds(Integer.parseInt(editTextICUTotal.getText().toString()));
                }
                if(!editTextICUVacant.getText().toString().isEmpty()){
                    hospital.setmTotalNoOfCovidBeds(Integer.parseInt(editTextICUVacant.getText().toString()));
                }
                if(!editTextNormalTotal.getText().toString().isEmpty()){
                    hospital.setmTotalNoOfCovidBeds(Integer.parseInt(editTextNormalTotal.getText().toString()));
                }
                if(!editTextNormalVacant.getText().toString().isEmpty()){
                    hospital.setmTotalNoOfCovidBeds(Integer.parseInt(editTextNormalVacant.getText().toString()));
                }

                FirebaseFirestore.getInstance()
                        .collection("Hospitals")
                        .document(UId).set(hospital).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ChangeNumberOfBeds.this,"Changed number of beds.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChangeNumberOfBeds.this, "Failed setting total beds", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    private void initUI() {
        editTextNormalTotal = findViewById(R.id.edittext_normal_total);
        editTextNormalVacant = findViewById(R.id.edittext_normal_vacant);
        editTextICUTotal = findViewById(R.id.edittext_icu_total);
        editTextICUVacant = findViewById(R.id.edittext_icu_vacant);
        editTextCovidVacant = findViewById(R.id.edittext_covid_vacant);
        editTextCovidTotal = findViewById(R.id.edittext_covid_total);
    }
}