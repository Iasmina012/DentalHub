package com.upt.cti.dentalhub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.upt.cti.dentalhub.Appointments.Activity_SelectAppointments;
import com.upt.cti.dentalhub.Menus.MainMenuActivity;

public class MainActivity extends MainMenuActivity {

    LinearLayout DoctorsInfo;
    LinearLayout Services;
    LinearLayout Appointment;
    LinearLayout Contact;
    LinearLayout Tech;
    LinearLayout Symptoms;
    LinearLayout Care;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DoctorsInfo = findViewById(R.id.linearlayout1);
        Services = findViewById(R.id.linearlayout2);
        Appointment = findViewById(R.id.linearlayout3);
        Contact = findViewById(R.id.linearlayout4);
        Tech = findViewById(R.id.linearlayout5);
        Symptoms = findViewById(R.id.linearlayout6);
        Care = findViewById(R.id.linearlayout7);

        DoctorsInfo.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), Activity_DoctorsInfo.class);
            i.putExtra("table_name", "Doctors");
            startActivity(i);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });

        Services.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), Activity_Services.class);
            i.putExtra("table_name", "Services");
            startActivity(i);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });

        Appointment.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), Activity_SelectAppointments.class);
            i.putExtra("table_name", "Appointment");
            startActivity(i);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });

        Contact.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), Activity_Contact.class);
            i.putExtra("table_name", "Contact");
            startActivity(i);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });

        Tech.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), Activity_Tech.class);
            i.putExtra("table_name", "Technologies");
            startActivity(i);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });

        Symptoms.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), Activity_SelectSymptoms.class);
            i.putExtra("table_name", "Symptoms Checker");
            startActivity(i);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });

        Care.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), Activity_Tips.class);
            i.putExtra("table_name", "Teeth Care");
            startActivity(i);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });

    }

}