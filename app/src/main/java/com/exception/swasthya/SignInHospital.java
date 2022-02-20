package com.exception.swasthya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignInHospital extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Button button;
    EditText EditTextpassword;
    EditText EditTextemail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_hospital);

        button = findViewById(R.id.button_sign_in_account);
        EditTextpassword = findViewById(R.id.editText_user_name_sign_in);
        EditTextemail = findViewById(R.id.editText_hospital_password_sign_in);




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog dialog = new ProgressDialog(SignInHospital.this);
                dialog.setTitle("Please wait!");
                dialog.setMessage("While we are Logging in");

                dialog.show();
                String email = EditTextemail.getText().toString();
                String password = EditTextpassword.getText().toString();
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser == null) {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignInHospital.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        //Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        SharedPreferences preferences = SignInHospital.this.getSharedPreferences("MyPref", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.remove("isHospital").apply();
                                        FirebaseFirestore.getInstance().collection("Hospitals")
                                                .document(mAuth.getUid())
                                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                dialog.dismiss();
                                                if (task.getResult().exists()) {
                                                    SharedPreferences.Editor editor = preferences.edit();
                                                    editor.putBoolean("isHospital", true).apply();
                                                    Toast.makeText(SignInHospital.this, "Logged in as hospital successful",Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(SignInHospital.this, MainActivity.class));
                                                } else {

                                                    Toast.makeText(SignInHospital.this, "You are not registered as a hospital... Signing out",Toast.LENGTH_LONG).show();
                                                    FirebaseAuth.getInstance().signOut();
                                                    startActivity(new Intent(SignInHospital.this, SignUpActivity.class));
                                                         }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                dialog.dismiss();
                                                Toast.makeText(SignInHospital.this, "Try logging in after some time",Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }else{
                                        Toast.makeText(SignInHospital.this, "Retry after some time",Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }


                                }
                            });
                }else{
                    Toast.makeText(SignInHospital.this, "Log out first",
                            Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });





    }
}