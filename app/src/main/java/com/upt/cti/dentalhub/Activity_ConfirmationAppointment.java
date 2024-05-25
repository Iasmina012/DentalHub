package com.upt.cti.dentalhub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Activity_ConfirmationAppointment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_appointment);

        TextView textViewConfirmation = findViewById(R.id.textViewConfirmation);
        Button buttonGoToAppointment = findViewById(R.id.buttonGoToAppointment);
        ImageView buttonClose = findViewById(R.id.buttonClose);

        Intent intent = getIntent();
        String doctorName = intent.getStringExtra("selectedDoctor");
        String appointmentDate = intent.getStringExtra("selectedDate");
        String appointmentTime = intent.getStringExtra("selectedTime");

        String confirmationText = "You booked an appointment with " + doctorName + " on " + appointmentDate + " at " + appointmentTime + ".";
        textViewConfirmation.setText(confirmationText);

        buttonGoToAppointment.setOnClickListener(v -> {
            Intent appointmentIntent = new Intent(Activity_ConfirmationAppointment.this, Activity_ViewAppointment.class);
            startActivity(appointmentIntent);
        });

        buttonClose.setOnClickListener(v -> {
            Intent mainIntent = new Intent(Activity_ConfirmationAppointment.this, MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mainIntent);
            finish();
        });

    }

}