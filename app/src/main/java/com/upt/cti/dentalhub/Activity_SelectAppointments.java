package com.upt.cti.dentalhub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Activity_SelectAppointments extends AppCompatActivity {

    private static final String TAG = "SelectAppointments";
    private Button buttonBookAppointment, buttonViewAppointment;
    private TextView textViewWelcome;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_appointments);

        buttonBookAppointment = findViewById(R.id.buttonBookAppointment);
        buttonViewAppointment = findViewById(R.id.buttonViewAppointment);
        textViewWelcome = findViewById(R.id.textViewWelcome);

        db = FirebaseDatabase.getInstance("https://dentalhub-1a0c0-default-rtdb.europe-west1.firebasedatabase.app").getReference("users");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            db.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        String displayName = dataSnapshot.child("displayName").getValue(String.class);
                        if (displayName != null) {
                            textViewWelcome.setText("Welcome, " + displayName + "!");
                        } else {
                            Log.d(TAG, "Username not found.");
                            textViewWelcome.setText("Welcome!");
                        }
                    } else {
                        Log.d(TAG, "User data not found.");
                        textViewWelcome.setText("Welcome!");
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    textViewWelcome.setText("Welcome!");
                    Log.e(TAG, "Database error: " + databaseError.getMessage());

                }
            });
        } else {
            textViewWelcome.setText("Welcome!");
        }

        buttonBookAppointment.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_SelectAppointments.this, Activity_SelectLocation.class);
            startActivity(intent);
        });

        buttonViewAppointment.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_SelectAppointments.this, Activity_ViewAppointment.class);
            startActivity(intent);
        });

    }

}