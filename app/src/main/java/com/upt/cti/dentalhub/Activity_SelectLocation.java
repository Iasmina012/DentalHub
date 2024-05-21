package com.upt.cti.dentalhub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Activity_SelectLocation extends BaseActivity {

    private RadioGroup radioGroupLocations;
    private Button buttonNext, buttonBack;
    private String selectedLocation;
    private String appointmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);

        radioGroupLocations = findViewById(R.id.radioGroupLocations);
        buttonNext = findViewById(R.id.buttonNext);
        buttonBack = findViewById(R.id.buttonBack);

        Intent intent = getIntent();
        appointmentId = intent.getStringExtra("appointmentId");
        selectedLocation = intent.getStringExtra("selectedLocation");

        if (appointmentId != null) {
            Log.d("Activity_SelectLocation", "Appointment ID received: " + appointmentId);
        } else {
            Log.e("Activity_SelectLocation", "Failed to retrieve appointment ID");
        }

        addLocationOptions();

        buttonNext.setOnClickListener(v -> {
            int selectedId = radioGroupLocations.getCheckedRadioButtonId();
            if (selectedId != -1) {
                RadioButton selectedRadioButton = findViewById(selectedId);
                selectedLocation = selectedRadioButton.getText().toString();

                Intent nextIntent = new Intent(Activity_SelectLocation.this, Activity_SelectDoctor.class);
                nextIntent.putExtra("selectedLocation", selectedLocation);
                nextIntent.putExtra("appointmentId", appointmentId);
                nextIntent.putExtra("selectedDentist", getIntent().getStringExtra("selectedDentist"));
                nextIntent.putExtra("selectedService", getIntent().getStringExtra("selectedService"));
                nextIntent.putExtra("selectedDate", getIntent().getStringExtra("selectedDate"));
                nextIntent.putExtra("selectedTime", getIntent().getStringExtra("selectedTime"));
                nextIntent.putExtra("selectedInsurance", getIntent().getStringExtra("selectedInsurance"));
                startActivity(nextIntent);
            } else {
                Toast.makeText(Activity_SelectLocation.this, "Please select a location!", Toast.LENGTH_SHORT).show();
            }
        });

        buttonBack.setOnClickListener(v -> onBackPressed());
    }

    private void addLocationOptions() {

        String[] locations = {"425 Broadway Suite 22\nNew York, NY 10018\nClinic 1", "58 Wall Street Suite 100\nNew York, NY 10005\nClinic 2"};
        for (String location : locations) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(location);
            radioButton.setTextSize(20);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 18);
            radioButton.setLayoutParams(params);
            radioGroupLocations.addView(radioButton);

            if (location.equals(selectedLocation)) {
                radioButton.setChecked(true);
            }
        }

    }

}