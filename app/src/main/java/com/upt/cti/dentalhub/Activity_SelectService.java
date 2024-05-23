package com.upt.cti.dentalhub;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

public class Activity_SelectService extends BaseActivity {

    private GridLayout gridLayout;
    private Button buttonNext, buttonBack;
    private Button previouslySelectedButton;
    private String appointmentId;
    private String selectedLocation;
    private String selectedDoctor;
    private int selectedDoctorId;
    private String selectedSpecialization;
    private String selectedService;
    private String selectedDate;
    private String selectedTime;
    private String selectedInsurance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_service);

        gridLayout = findViewById(R.id.gridLayoutServices);
        buttonNext = findViewById(R.id.buttonNext);
        buttonBack = findViewById(R.id.buttonBack);

        Intent intent = getIntent();
        appointmentId = intent.getStringExtra("appointmentId");
        selectedLocation = intent.getStringExtra("selectedLocation");
        selectedDoctor = intent.getStringExtra("selectedDoctor");
        selectedDoctorId = intent.getIntExtra("selectedDoctorId", -1);
        selectedSpecialization = getDoctorSpecialization(selectedDoctorId);
        selectedService = intent.getStringExtra("selectedService");
        selectedDate = intent.getStringExtra("selectedDate");
        selectedTime = intent.getStringExtra("selectedTime");
        selectedInsurance = intent.getStringExtra("selectedInsurance");

        if (appointmentId != null) {
            Log.d("Activity_SelectService", "Appointment ID received: " + appointmentId);
        } else {
            Log.e("Activity_SelectService", "Failed to retrieve appointment ID");
        }

        if (selectedSpecialization == null) {
            Log.e("Activity_SelectService", "selectedSpecialization is null");
            Toast.makeText(this, "Specialization not found for the selected doctor", Toast.LENGTH_LONG).show();
            return;
        }

        addServiceOptions();

        buttonNext.setOnClickListener(v -> {
            if (selectedService != null) {
                Intent nextIntent = new Intent(Activity_SelectService.this, Activity_SelectDateTime.class);
                nextIntent.putExtra("appointmentId", appointmentId);
                nextIntent.putExtra("selectedLocation", selectedLocation);
                nextIntent.putExtra("selectedDoctor", selectedDoctor);
                nextIntent.putExtra("selectedDoctorId", selectedDoctorId);
                nextIntent.putExtra("selectedSpecialization", selectedSpecialization);
                nextIntent.putExtra("selectedService", selectedService);
                nextIntent.putExtra("selectedDate", selectedDate);
                nextIntent.putExtra("selectedTime", selectedTime);
                nextIntent.putExtra("selectedInsurance", selectedInsurance);
                startActivity(nextIntent);
            } else {
                Toast.makeText(Activity_SelectService.this, "Please select a service!", Toast.LENGTH_SHORT).show();
            }
        });

        buttonBack.setOnClickListener(v -> onBackPressed());

    }

    private void addServiceOptions() {

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT s.* FROM " + DatabaseHelper.TABLE_SERVICES + " s "
                + "INNER JOIN " + DatabaseHelper.TABLE_SERVICE_SPECIALIZATIONS + " ss "
                + "ON s." + DatabaseHelper.COLUMN_ID + " = ss." + DatabaseHelper.COLUMN_SERVICE_ID + " "
                + "WHERE ss." + DatabaseHelper.COLUMN_SPECIALIZATION + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{selectedSpecialization});

        if (cursor != null) {
            Log.d("Activity_SelectService", "Number of services found: " + cursor.getCount());
            while (cursor.moveToNext()) {
                String serviceName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SERVICE_NAME));

                Log.d("Activity_SelectService", "Service found: " + serviceName);

                Button button = new Button(this);
                button.setText(serviceName);
                button.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_bg_light_teal));
                button.setPadding(16, 16, 16, 16);
                button.setTextColor(ContextCompat.getColor(this, R.color.grey));

                button.setOnClickListener(v -> {
                    if (button == previouslySelectedButton) {
                        deselectButton(button);
                        selectedService = null;
                        previouslySelectedButton = null;
                    } else {
                        selectedService = serviceName;
                        highlightSelectedButton(button);
                    }
                });

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
                params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
                params.setMargins(8, 8, 8, 8);
                button.setLayoutParams(params);

                gridLayout.addView(button);

                if (serviceName.equals(selectedService)) {
                    highlightSelectedButton(button);
                }
            }
            cursor.close();
        } else {
            Log.e("Activity_SelectService", "Cursor is null.");
        }

        db.close();

    }

    private String getDoctorSpecialization(int doctorId) {

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String specialization = null;

        String query = "SELECT " + DatabaseHelper.COLUMN_DOCTOR_SPECIALIZATION + " FROM " + DatabaseHelper.TABLE_DOCTORS + " WHERE " + DatabaseHelper.COLUMN_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(doctorId)});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                specialization = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DOCTOR_SPECIALIZATION));
            }
            cursor.close();
        }

        db.close();
        return specialization;

    }

    private void highlightSelectedButton(Button selectedButton) {

        if (previouslySelectedButton != null) {
            deselectButton(previouslySelectedButton);
        }

        selectedButton.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_bg_dark_teal));
        selectedButton.setTextColor(ContextCompat.getColor(this, R.color.white));

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.scale);
        selectedButton.startAnimation(anim);

        previouslySelectedButton = selectedButton;

    }

    private void deselectButton(Button button) {

        button.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_bg_light_teal));
        button.setTextColor(ContextCompat.getColor(this, R.color.grey));

        Animation reverseAnim = AnimationUtils.loadAnimation(this, R.anim.scale_reverse);
        button.startAnimation(reverseAnim);

    }

}