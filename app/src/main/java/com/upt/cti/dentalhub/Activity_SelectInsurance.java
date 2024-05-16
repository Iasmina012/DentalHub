package com.upt.cti.dentalhub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

public class Activity_SelectInsurance extends AppCompatActivity {

    private static final String TAG = "Activity_SelectInsurance";

    private RadioGroup radioGroupInsurance;
    private Button buttonBook, buttonBack;
    private DatabaseReference db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String selectedDentist, selectedService, selectedDate, selectedTime, selectedInsurance, selectedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_insurance);

        radioGroupInsurance = findViewById(R.id.radioGroupInsurance);
        buttonBook = findViewById(R.id.buttonBook);
        buttonBack = findViewById(R.id.buttonBack);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://dentalhub-1a0c0-default-rtdb.europe-west1.firebasedatabase.app");
        db = database.getReference();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "User is not authenticated!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Intent intent = getIntent();
        selectedDentist = intent.getStringExtra("selectedDentist");
        selectedService = intent.getStringExtra("selectedService");
        selectedDate = intent.getStringExtra("selectedDate");
        selectedTime = intent.getStringExtra("selectedTime");
        selectedLocation = intent.getStringExtra("selectedLocation");

        addInsuranceOptions();

        for (int i = 0; i < radioGroupInsurance.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) radioGroupInsurance.getChildAt(i);
            radioButton.setTextSize(20);
        }

        buttonBook.setOnClickListener(v -> {
            int selectedId = radioGroupInsurance.getCheckedRadioButtonId();
            if (selectedId != -1) {
                RadioButton selectedRadioButton = findViewById(selectedId);
                selectedInsurance = selectedRadioButton.getText().toString();

                Log.d(TAG, "Insurance selected: " + selectedInsurance);
                bookAppointment();
            } else {
                Toast.makeText(Activity_SelectInsurance.this, "Please select an insurance type!", Toast.LENGTH_SHORT).show();
            }
        });

        buttonBack.setOnClickListener(v -> onBackPressed());

    }

    private void addInsuranceOptions() {

        String[] insurances = {"National Health Insurance Fund", "Groupama", "NN", "Medicover", "UltraMED", "Euraxess", "Raiffeisen", "Generali", "No Insurance"};
        for (String insurance : insurances) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(insurance);

            radioButton.setTextSize(20);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 18);
            radioButton.setLayoutParams(params);

            radioGroupInsurance.addView(radioButton);
        }

    }

    private void bookAppointment() {

        String appointmentId = db.child("appointments").push().getKey();
        Map<String, Object> appointment = new HashMap<>();
        appointment.put("location", selectedLocation);
        appointment.put("dentist", selectedDentist);
        appointment.put("service", selectedService);
        appointment.put("date", selectedDate);
        appointment.put("time", selectedTime);
        appointment.put("insurance", selectedInsurance);
        appointment.put("userId", currentUser.getUid());

        if (appointmentId != null) {
            db.child("appointments").child(appointmentId).setValue(appointment)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(Activity_SelectInsurance.this, "Appointment booked successfully", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Appointment booked successfully");

                        Intent intent = new Intent(Activity_SelectInsurance.this, Activity_ConfirmationAppointment.class);
                        appointment.put("location", selectedLocation);
                        intent.putExtra("selectedDentist", selectedDentist);
                        intent.putExtra("selectedDate", selectedDate);
                        intent.putExtra("selectedTime", selectedTime);
                        intent.putExtra("selectedLocation", selectedLocation);
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(Activity_SelectInsurance.this, "Failed to book appointment", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Failed to book appointment", e);
                    });
        } else {
            Toast.makeText(Activity_SelectInsurance.this, "Failed to generate appointment ID", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Failed to generate appointment ID");
        }

    }

}