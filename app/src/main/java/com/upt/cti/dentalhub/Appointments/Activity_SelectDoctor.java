package com.upt.cti.dentalhub.Appointments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.upt.cti.dentalhub.Database.DatabaseHelper;
import com.upt.cti.dentalhub.R;
import com.upt.cti.dentalhub.Menus.PromptMenuActivity;

public class Activity_SelectDoctor extends PromptMenuActivity {

    private RadioGroup radioGroupDoctors;
    private Button buttonNext, buttonBack;
    private String appointmentId;
    private String selectedLocation;
    private String selectedDoctor;
    private int selectedDoctorId;
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
        setContentView(R.layout.activity_select_doctor);

        radioGroupDoctors = findViewById(R.id.radioGroupDoctors);
        buttonNext = findViewById(R.id.buttonNext);
        buttonBack = findViewById(R.id.buttonBack);

        Intent intent = getIntent();
        appointmentId = intent.getStringExtra("appointmentId");
        selectedLocation = intent.getStringExtra("selectedLocation");
        selectedDoctor = intent.getStringExtra("selectedDoctor");
        selectedDoctorId = intent.getIntExtra("selectedDoctorId", -1);
        selectedService = intent.getStringExtra("selectedService");
        selectedDate = intent.getStringExtra("selectedDate");
        selectedTime = intent.getStringExtra("selectedTime");
        selectedInsurance = intent.getStringExtra("selectedInsurance");
        selectedFirstName = intent.getStringExtra("selectedFirstName");
        selectedLastName = intent.getStringExtra("selectedLastName");
        userId = intent.getStringExtra("userId");

        if (appointmentId != null) {
            Log.d("Activity_SelectDoctor", "Appointment ID received: " + appointmentId);
        } else {
            Log.e("Activity_SelectDoctor", "Failed to retrieve appointment ID");
        }

        addDoctorOptions();

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
                selectedDoctorId = selectedRadioButton.getId();

                DatabaseHelper dbHelper = new DatabaseHelper(this);
                String selectedSpecialization = dbHelper.getDoctorSpecialization(selectedDoctorId);

                Intent nextIntent = new Intent(Activity_SelectDoctor.this, Activity_SelectService.class);
                nextIntent.putExtra("appointmentId", appointmentId);
                nextIntent.putExtra("selectedLocation", selectedLocation);
                nextIntent.putExtra("selectedDoctor", selectedDoctor);
                nextIntent.putExtra("selectedDoctorId", selectedDoctorId);
                nextIntent.putExtra("selectedSpecialization", selectedSpecialization);
                nextIntent.putExtra("selectedService", selectedService);
                nextIntent.putExtra("selectedDate", selectedDate);
                nextIntent.putExtra("selectedTime", selectedTime);
                nextIntent.putExtra("selectedInsurance", selectedInsurance);
                nextIntent.putExtra("selectedFirstName", selectedFirstName);
                nextIntent.putExtra("selectedLastName", selectedLastName);
                nextIntent.putExtra("userId", userId);
                startActivity(nextIntent);
            } else {
                Toast.makeText(Activity_SelectDoctor.this, "Please select a doctor!", Toast.LENGTH_SHORT).show();
            }

        });

        buttonBack.setOnClickListener(v -> onBackPressed());

    }

    private void addDoctorOptions() {

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        long locationId = getLocationIdByAddress(db, selectedLocation);

        if (locationId != -1) {
            Cursor cursor = db.rawQuery(
                    "SELECT d." + DatabaseHelper.COLUMN_ID + ", d." + DatabaseHelper.COLUMN_DOCTOR_NAME + ", d." + DatabaseHelper.COLUMN_DOCTOR_IMAGE +
                            " FROM " + DatabaseHelper.TABLE_DOCTORS + " d" +
                            " INNER JOIN " + DatabaseHelper.TABLE_DOCTOR_LOCATION + " dl ON d." + DatabaseHelper.COLUMN_ID + " = dl." + DatabaseHelper.COLUMN_DOCTOR_ID +
                            " WHERE dl." + DatabaseHelper.COLUMN_LOCATION_ID + " = ?",
                    new String[]{String.valueOf(locationId)}
            );

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DOCTOR_NAME));
                    int image = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DOCTOR_IMAGE));

                    RadioButton radioButton = new RadioButton(this);
                    radioButton.setId(id);
                    radioButton.setText(name);
                    radioButton.setTextSize(20);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(0, 0, 0, 18);
                    radioButton.setLayoutParams(params);

                    Drawable drawable = ContextCompat.getDrawable(this, image);
                    if (drawable != null) {
                        int width = 150;
                        int height = 150;
                        drawable.setBounds(0, 0, width, height);
                        radioButton.setCompoundDrawables(drawable, null, null, null);
                    } else {
                        Log.e("Activity_SelectDoctor", "Drawable not found for doctor: " + name);
                    }

                    radioGroupDoctors.addView(radioButton);

                    if (name.equals(selectedDoctor)) {
                        radioButton.setChecked(true);
                    }
                }
                cursor.close();
            }

        } else {
            Log.e("Activity_SelectDoctor", "Invalid location ID for address: " + selectedLocation);
        }

        db.close();

    }

    private long getLocationIdByAddress(SQLiteDatabase db, String address) {

        long locationId = -1;
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_LOCATIONS,
                new String[]{DatabaseHelper.COLUMN_ID},
                DatabaseHelper.COLUMN_LOCATION_ADDRESS + "=?",
                new String[]{address},
                null, null, null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                locationId = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
            }
            cursor.close();
        }

        return locationId;

    }

}