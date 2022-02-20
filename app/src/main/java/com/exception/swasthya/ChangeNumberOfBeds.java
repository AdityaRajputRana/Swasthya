package com.exception.swasthya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChangeNumberOfBeds extends AppCompatActivity {

    EditText editTextNormalTotal;
    EditText editTextNormalVacant;
    EditText editTextICUTotal;
    EditText editTextICUVacant;
    EditText editTextCovidVacant;
    EditText editTextCovidTotal;
    Hospital hospital;
    FirebaseUser user;
    TextView hospitalName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_number_of_beds);


        initUI();
        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore.getInstance()
                .collection("Hospitals")
                .document(user.getUid())//document ki id is uid
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){

                            try {
                                hospital = task.getResult().toObject(Hospital.class);
                                editTextCovidTotal.setText(String.valueOf(hospital != null ? hospital.getmTotalNoOfCovidBeds() : 0));
                                editTextCovidVacant.setText(String.valueOf(hospital.getmVacantCovidBeds()));
                                editTextNormalTotal.setText(String.valueOf(hospital.getmTotalNormalBeds()));
                                editTextNormalVacant.setText(String.valueOf(hospital.getmVacantNormalBeds()));
                                editTextICUVacant.setText(String.valueOf(hospital.getmVacantICUBeds()));
                                editTextICUTotal.setText(String.valueOf(hospital.getmTotalICUBeds()));
                                hospitalName.setText(String.valueOf(hospital.getmHospitalName()));
                            }catch (NullPointerException e){
                                e.printStackTrace();

                            }

                    }
                }).addOnFailureListener(e -> Toast.makeText(ChangeNumberOfBeds.this, "Failed fetching total beds", Toast.LENGTH_SHORT).show());





        Button button = findViewById(R.id.button_update_beds);
        button.setOnClickListener(view -> {


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
                    .document(user.getUid()).set(hospital).addOnCompleteListener(task -> {
                        Toast.makeText(ChangeNumberOfBeds.this,"Changed number of beds.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ChangeNumberOfBeds.this , MainActivity.class ));
                        finish();
                    }).addOnFailureListener(e -> Toast.makeText(ChangeNumberOfBeds.this, "Failed setting total beds", Toast.LENGTH_SHORT).show());
        });


    }

    private void initUI() {
        editTextNormalTotal = findViewById(R.id.edittext_normal_total);
        editTextNormalVacant = findViewById(R.id.edittext_normal_vacant);
        editTextICUTotal = findViewById(R.id.edittext_icu_total);
        editTextICUVacant = findViewById(R.id.edittext_icu_vacant);
        editTextCovidVacant = findViewById(R.id.edittext_covid_vacant);
        editTextCovidTotal = findViewById(R.id.edittext_covid_total);
        hospitalName= findViewById(R.id.hospitalName);
    }
}