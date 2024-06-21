package com.upt.cti.dentalhub.Appointments;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.upt.cti.dentalhub.R;
import com.upt.cti.dentalhub.Menus.PromptMenuActivity;

public class Activity_SelectPatientName extends PromptMenuActivity {

    private EditText editTextFirstName, editTextLastName;
    private Button buttonNext, buttonBack;
    private String appointmentId;
    private String selectedLocation;
    private String selectedDoctor;
    private String selectedService;
    private String selectedDate;
    private String selectedTime;
    private String selectedInsurance;
    private String selectedFirstName;
    private String selectedLastName;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_patient_name);

        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        buttonNext = findViewById(R.id.buttonNext);
        buttonBack = findViewById(R.id.buttonBack);

        Intent intent = getIntent();
        appointmentId = intent.getStringExtra("appointmentId");
        selectedLocation = intent.getStringExtra("selectedLocation");
        selectedDoctor = intent.getStringExtra("selectedDoctor");
        selectedService = intent.getStringExtra("selectedService");
        selectedDate = intent.getStringExtra("selectedDate");
        selectedTime = intent.getStringExtra("selectedTime");
        selectedInsurance = intent.getStringExtra("selectedInsurance");
        selectedFirstName = intent.getStringExtra("selectedFirstName");
        selectedLastName = intent.getStringExtra("selectedLastName");
        userId = intent.getStringExtra("userId");

        //preselected fields
        if (selectedFirstName != null) {
            editTextFirstName.setText(selectedFirstName);
        }
        if (selectedLastName != null) {
            editTextLastName.setText(selectedLastName);
        }

        buttonNext.setOnClickListener(v -> {

            String firstName = editTextFirstName.getText().toString().trim();
            String lastName = editTextLastName.getText().toString().trim();

            if (firstName.isEmpty() || lastName.isEmpty()) {
                Toast.makeText(Activity_SelectPatientName.this, "Please enter both your first and last name!", Toast.LENGTH_SHORT).show();
            } else {
                Intent nextIntent = new Intent(Activity_SelectPatientName.this, Activity_SelectInsurance.class);
                nextIntent.putExtra("appointmentId", appointmentId);
                nextIntent.putExtra("selectedLocation", selectedLocation);
                nextIntent.putExtra("selectedDoctor", selectedDoctor);
                nextIntent.putExtra("selectedService", selectedService);
                nextIntent.putExtra("selectedDate", selectedDate);
                nextIntent.putExtra("selectedTime", selectedTime);
                nextIntent.putExtra("selectedInsurance", selectedInsurance);
                nextIntent.putExtra("selectedFirstName", editTextFirstName.getText().toString().trim());
                nextIntent.putExtra("selectedLastName", editTextLastName.getText().toString().trim());
                nextIntent.putExtra("userId", userId);
                startActivity(nextIntent);
            }

        });

        buttonBack.setOnClickListener(v -> {

            Intent backIntent = new Intent();
            backIntent.putExtra("selectedFirstName", editTextFirstName.getText().toString().trim());
            backIntent.putExtra("selectedLastName", editTextLastName.getText().toString().trim());
            setResult(RESULT_OK, backIntent);
            finish();

        });

    }

}