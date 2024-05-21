package com.upt.cti.dentalhub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;

public class Activity_SelectDateTime extends BaseActivity {

    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button buttonNext, buttonBack;
    private String selectedDate;
    private String selectedTime;
    private String appointmentId;

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
        selectedDate = intent.getStringExtra("selectedDate");
        selectedTime = intent.getStringExtra("selectedTime");

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

            Calendar selectedCalendar = Calendar.getInstance();
            selectedCalendar.set(year, month, day, hour, minute, 0);

            if (selectedCalendar.before(calendar)) {
                Toast.makeText(Activity_SelectDateTime.this, "Please select a valid date and time!", Toast.LENGTH_SHORT).show();
                return;
            }

            selectedDate = day + "/" + (month + 1) + "/" + year;
            selectedTime = hour + ":" + (minute < 10 ? "0" + minute : minute);

            Intent nextIntent = new Intent(Activity_SelectDateTime.this, Activity_SelectInsurance.class);
            nextIntent.putExtra("selectedDate", selectedDate);
            nextIntent.putExtra("selectedTime", selectedTime);
            nextIntent.putExtra("selectedDoctor", getIntent().getStringExtra("selectedDoctor"));
            nextIntent.putExtra("selectedService", getIntent().getStringExtra("selectedService"));
            nextIntent.putExtra("selectedLocation", getIntent().getStringExtra("selectedLocation"));
            nextIntent.putExtra("appointmentId", appointmentId);
            nextIntent.putExtra("selectedInsurance", getIntent().getStringExtra("selectedInsurance"));
            startActivity(nextIntent);
        });

        buttonBack.setOnClickListener(v -> onBackPressed());

    }

}