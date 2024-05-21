package com.upt.cti.dentalhub;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

public class Activity_SelectDoctor extends BaseActivity {

    private RadioGroup radioGroupDoctors;
    private Button buttonNext, buttonBack;
    private String selectedLocation;
    private String selectedDoctor;
    private String appointmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_doctor);

        radioGroupDoctors = findViewById(R.id.radioGroupDoctors);
        buttonNext = findViewById(R.id.buttonNext);
        buttonBack = findViewById(R.id.buttonBack);

        Intent intent = getIntent();
        selectedLocation = intent.getStringExtra("selectedLocation");
        selectedDoctor = intent.getStringExtra("selectedDoctor");
        appointmentId = intent.getStringExtra("appointmentId");

        if (appointmentId != null) {
            Log.d("Activity_SelectDoctor", "Appointment ID received: " + appointmentId);
        } else {
            Log.e("Activity_SelectDoctor", "Failed to retrieve appointment ID");
        }

        addDoctorsOptions();

        for (int i = 0; i < radioGroupDoctors.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) radioGroupDoctors.getChildAt(i);
            radioButton.setTextSize(20);

            if (radioButton.getText().toString().equals(selectedDoctor)) {
                radioButton.setChecked(true);
            }
        }

        buttonNext.setOnClickListener(v -> {
            int selectedId = radioGroupDoctors.getCheckedRadioButtonId();
            if (selectedId != -1) {
                RadioButton selectedRadioButton = findViewById(selectedId);
                selectedDoctor = selectedRadioButton.getText().toString();

                Intent nextIntent = new Intent(Activity_SelectDoctor.this, Activity_SelectService.class);
                nextIntent.putExtra("selectedDoctor", selectedDoctor);
                nextIntent.putExtra("selectedLocation", selectedLocation);
                nextIntent.putExtra("appointmentId", appointmentId);
                nextIntent.putExtra("selectedService", getIntent().getStringExtra("selectedService"));
                nextIntent.putExtra("selectedDate", getIntent().getStringExtra("selectedDate"));
                nextIntent.putExtra("selectedTime", getIntent().getStringExtra("selectedTime"));
                nextIntent.putExtra("selectedInsurance", getIntent().getStringExtra("selectedInsurance"));
                startActivity(nextIntent);
            } else {
                Toast.makeText(Activity_SelectDoctor.this, "Please select a doctor!", Toast.LENGTH_SHORT).show();
            }
        });

        buttonBack.setOnClickListener(v -> onBackPressed());

    }

    private void addDoctorsOptions() {

        String[] doctors = {"Dr. Daniela Pop", "Dr. Ana Maria Popescu", "Dr. Maria Ionescu", "Dr. Andrei Radu", "Dr. Elena Popa", "Any"};
        int[] images = {R.drawable.doctor01, R.drawable.doctor02, R.drawable.doctor04, R.drawable.doctor03, R.drawable.doctor05, R.drawable.doctors};

        for (int i = 0; i < doctors.length; i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(doctors[i]);
            radioButton.setTextSize(20);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 18);
            radioButton.setLayoutParams(params);

            Drawable drawable = ContextCompat.getDrawable(this, images[i]);
            if (drawable != null) {
                int width = 150;
                int height = 150;
                drawable.setBounds(0, 0, width, height);
                radioButton.setCompoundDrawables(drawable, null, null, null);
            } else {
                Log.e("Activity_SelectDoctor", "Drawable not found for index: " + i);
            }

            radioGroupDoctors.addView(radioButton);
        }

    }

}