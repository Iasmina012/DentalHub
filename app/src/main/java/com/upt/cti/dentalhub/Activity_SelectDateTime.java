package com.upt.cti.dentalhub;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Activity_SelectDateTime extends PromptMenuActivity {

    private DatePicker datePicker;
    private TimePicker timePicker;
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
        setContentView(R.layout.activity_select_date_time);

        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        buttonNext = findViewById(R.id.buttonNext);
        buttonBack = findViewById(R.id.buttonBack);

        timePicker.setIs24HourView(true);
        Calendar calendar = Calendar.getInstance();
        datePicker.setMinDate(calendar.getTimeInMillis());

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
            Log.d("Activity_SelectDateTime", "Appointment ID received: " + appointmentId);
        } else {
            Log.e("Activity_SelectDateTime", "Failed to retrieve appointment ID");
        }

        if (selectedDate != null && selectedTime != null) {

            String[] dateParts = selectedDate.split("/");
            int day = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]) - 1;
            int year = Integer.parseInt(dateParts[2]);

            String[] timeParts = selectedTime.split(":");
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);

            datePicker.updateDate(year, month, day);
            timePicker.setHour(hour);
            timePicker.setMinute(minute);

        }

        buttonNext.setOnClickListener(v -> {

            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth();
            int year = datePicker.getYear();
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();

            selectedDate = day + "/" + (month + 1) + "/" + year;
            selectedTime = hour + ":" + (minute < 10 ? "0" + minute : minute);

            DatabaseHelper dbHelper = new DatabaseHelper(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            if (isDoctorAvailable(db, selectedDoctorId, selectedDate, selectedTime)) {
                Intent nextIntent = new Intent(Activity_SelectDateTime.this, Activity_SelectPatientName.class);
                nextIntent.putExtra("appointmentId", appointmentId);
                nextIntent.putExtra("selectedLocation", selectedLocation);
                nextIntent.putExtra("selectedDoctor", selectedDoctor);
                nextIntent.putExtra("selectedDoctorId", selectedDoctorId);
                nextIntent.putExtra("selectedService", selectedService);
                nextIntent.putExtra("selectedDate", selectedDate);
                nextIntent.putExtra("selectedTime", selectedTime);
                nextIntent.putExtra("selectedInsurance", selectedInsurance);;
                nextIntent.putExtra("selectedFirstName", selectedFirstName);
                nextIntent.putExtra("selectedLastName", selectedLastName);
                nextIntent.putExtra("userId", userId);
                startActivity(nextIntent);
            } else {
                Toast.makeText(Activity_SelectDateTime.this, "Doctor is not available at the selected time!", Toast.LENGTH_SHORT).show();
            }

            db.close();

        });

        buttonBack.setOnClickListener(v -> onBackPressed());

    }

    private boolean isDoctorAvailable(SQLiteDatabase db, int doctorId, String date, String time) {

        //parse date for the day of the week
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date parsedDate;

        try {
            parsedDate = dateFormat.parse(date);
        } catch (ParseException e) {
            Log.e("Activity_SelectDateTime", "Date parsing failed for date: " + date, e);
            return false;
        }

        if (parsedDate == null) {
            Log.e("Activity_SelectDateTime", "Parsed date is null for date: " + date);
            return false;
        }
        String dayOfWeek = new SimpleDateFormat("EEEE", Locale.getDefault()).format(parsedDate);

        //check schedule
        String scheduleQuery = "SELECT * FROM " + DatabaseHelper.TABLE_DOCTOR_SCHEDULE
                + " WHERE " + DatabaseHelper.COLUMN_DOCTOR_ID + "=? AND "
                + DatabaseHelper.COLUMN_DAY_OF_WEEK + "=?";
        Cursor cursor = db.rawQuery(scheduleQuery, new String[]{String.valueOf(doctorId), dayOfWeek});
        boolean isWithinSchedule = false;

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String startTime = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_START_TIME));
                String endTime = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_END_TIME));
                if (time.compareTo(startTime) >= 0 && time.compareTo(endTime) <= 0) {
                    isWithinSchedule = true;
                    break;
                }
            }
            cursor.close();
        }

        if (!isWithinSchedule) {
            return false;
        }

        //check for existing appointment
        String appointmentQuery = "SELECT * FROM " + DatabaseHelper.TABLE_APPOINTMENTS
                + " WHERE " + DatabaseHelper.COLUMN_APPOINTMENT_DOCTOR_ID + "=? AND "
                + DatabaseHelper.COLUMN_APPOINTMENT_DATE + "=? AND "
                + DatabaseHelper.COLUMN_APPOINTMENT_TIME + "=?";
        cursor = db.rawQuery(appointmentQuery, new String[]{String.valueOf(doctorId), date, time});
        boolean isAvailable = (cursor == null || cursor.getCount() == 0);
        if (cursor != null) {
            cursor.close();
        }

        return isAvailable;

    }

}