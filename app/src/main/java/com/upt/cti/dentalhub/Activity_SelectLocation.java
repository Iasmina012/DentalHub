package com.upt.cti.dentalhub;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    private String appointmentId;
    private String selectedLocation;
    private String selectedDoctor;
    private String selectedService;
    private String selectedDate;
    private String selectedTime;
    private String selectedInsurance;

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
        selectedDoctor = intent.getStringExtra("selectedDoctor");
        selectedService = intent.getStringExtra("selectedService");
        selectedDate = intent.getStringExtra("selectedDate");
        selectedTime = intent.getStringExtra("selectedTime");
        selectedInsurance = intent.getStringExtra("selectedInsurance");

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
                nextIntent.putExtra("appointmentId", appointmentId);
                nextIntent.putExtra("selectedLocation", selectedLocation);
                nextIntent.putExtra("selectedDoctor", selectedDoctor);
                nextIntent.putExtra("selectedService", selectedService);
                nextIntent.putExtra("selectedDate", selectedDate);
                nextIntent.putExtra("selectedTime", selectedTime);
                nextIntent.putExtra("selectedInsurance", selectedInsurance);
                startActivity(nextIntent);
            } else {
                Toast.makeText(Activity_SelectLocation.this, "Please select a location!", Toast.LENGTH_SHORT).show();
            }
        });

        buttonBack.setOnClickListener(v -> onBackPressed());

    }

    private void addLocationOptions() {

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_LOCATIONS, null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String address = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LOCATION_ADDRESS));

                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(address);
                radioButton.setTextSize(20);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 0, 18);

                radioButton.setLayoutParams(params);
                radioGroupLocations.addView(radioButton);

                if (address.equals(selectedLocation)) {
                    radioButton.setChecked(true);
                }
            }
            cursor.close();
        }

        db.close();

    }

}