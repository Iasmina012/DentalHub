package com.upt.cti.dentalhub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Activity_SelectDoctor extends AppCompatActivity {

    private RadioGroup radioGroupDentists;
    private Button buttonNext, buttonBack;
    private String selectedLocation;
    private String selectedDentist;
    private String appointmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_doctor);

        radioGroupDentists = findViewById(R.id.radioGroupDentists);
        buttonNext = findViewById(R.id.buttonNext);
        buttonBack = findViewById(R.id.buttonBack);

        Intent intent = getIntent();
        selectedLocation = intent.getStringExtra("selectedLocation");
        selectedDentist = intent.getStringExtra("selectedDentist");
        appointmentId = intent.getStringExtra("appointmentId");

        if (appointmentId != null) {
            Log.d("Activity_SelectDoctor", "Appointment ID received: " + appointmentId);
        } else {
            Log.e("Activity_SelectDoctor", "Failed to retrieve appointment ID");
        }

        addDentistOptions();

        for (int i = 0; i < radioGroupDentists.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) radioGroupDentists.getChildAt(i);
            radioButton.setTextSize(20);

            if (radioButton.getText().toString().equals(selectedDentist)) {
                radioButton.setChecked(true);
            }
        }

        buttonNext.setOnClickListener(v -> {
            int selectedId = radioGroupDentists.getCheckedRadioButtonId();
            if (selectedId != -1) {
                RadioButton selectedRadioButton = findViewById(selectedId);
                selectedDentist = selectedRadioButton.getText().toString();

                Intent nextIntent = new Intent(Activity_SelectDoctor.this, Activity_SelectService.class);
                nextIntent.putExtra("selectedDentist", selectedDentist);
                nextIntent.putExtra("selectedLocation", selectedLocation);
                nextIntent.putExtra("appointmentId", appointmentId);
                nextIntent.putExtra("selectedService", getIntent().getStringExtra("selectedService"));
                nextIntent.putExtra("selectedDate", getIntent().getStringExtra("selectedDate"));
                nextIntent.putExtra("selectedTime", getIntent().getStringExtra("selectedTime"));
                nextIntent.putExtra("selectedInsurance", getIntent().getStringExtra("selectedInsurance"));
                startActivity(nextIntent);
            } else {
                Toast.makeText(Activity_SelectDoctor.this, "Please select a dentist!", Toast.LENGTH_SHORT).show();
            }
        });

        buttonBack.setOnClickListener(v -> onBackPressed());

    }

    private void addDentistOptions() {

        String[] dentists = {"Dr. Daniela Pop", "Dr. Ana Maria Popescu", "Dr. Maria Ionescu", "Dr. Andrei Radu", "Dr. Elena Popa", "Any"};
        for (String dentist : dentists) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(dentist);
            radioButton.setTextSize(20);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 18);
            radioButton.setLayoutParams(params);
            radioGroupDentists.addView(radioButton);
        }

    }

}