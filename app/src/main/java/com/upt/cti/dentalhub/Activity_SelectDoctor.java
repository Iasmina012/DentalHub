package com.upt.cti.dentalhub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Activity_SelectDoctor extends AppCompatActivity {

    private RadioGroup radioGroupDentists;
    private Button buttonNext, buttonBack;
    private String selectedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_doctor);

        radioGroupDentists = findViewById(R.id.radioGroupDentists);
        buttonNext = findViewById(R.id.buttonNext);
        buttonBack = findViewById(R.id.buttonBack);

        Intent intent = getIntent();
        selectedLocation = intent.getStringExtra("selectedLocation");

        addDentistOptions();

        for (int i = 0; i < radioGroupDentists.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) radioGroupDentists.getChildAt(i);
            radioButton.setTextSize(20);
        }

        buttonNext.setOnClickListener(v -> {
            int selectedId = radioGroupDentists.getCheckedRadioButtonId();
            if (selectedId != -1) {
                RadioButton selectedRadioButton = findViewById(selectedId);
                String selectedDentist = selectedRadioButton.getText().toString();

                Intent nextIntent = new Intent(Activity_SelectDoctor.this, Activity_SelectService.class);
                nextIntent.putExtra("selectedDentist", selectedDentist);
                nextIntent.putExtra("selectedLocation", selectedLocation);
                startActivity(nextIntent);
            } else {
                Toast.makeText(Activity_SelectDoctor.this, "Please select a dentist!", Toast.LENGTH_SHORT).show();
            }
        });

        buttonBack.setOnClickListener(v -> onBackPressed());

    }

    private void addDentistOptions() {

        String[] dentists = {"Dr. Daniela Pop", "Dr. Ana Maria Popescu", "Dr. Maria Ionescu", "Dr. Andrei Radu", "Dr. Elena Popa", "Any"};
        for (String dentist : dentists) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(dentist);
            radioGroupDentists.addView(radioButton);
        }

    }

}