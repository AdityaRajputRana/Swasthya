package com.exception.swasthya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

                }
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