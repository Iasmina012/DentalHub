package com.upt.cti.dentalhub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class Activity_SelectDateTime extends AppCompatActivity {

    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button buttonNext, buttonBack;
    private String selectedDate;
    private String selectedTime;

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

            Intent intent = new Intent(Activity_SelectDateTime.this, Activity_SelectInsurance.class);
            intent.putExtra("selectedDate", selectedDate);
            intent.putExtra("selectedTime", selectedTime);
            intent.putExtra("selectedDentist", getIntent().getStringExtra("selectedDentist"));
            intent.putExtra("selectedService", getIntent().getStringExtra("selectedService"));
            startActivity(intent);
        });

        buttonBack.setOnClickListener(v -> onBackPressed());

    }

}