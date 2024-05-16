package com.upt.cti.dentalhub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Activity_SelectLocation extends AppCompatActivity {

    private RadioGroup radioGroupLocations;
    private Button buttonNext, buttonBack;
    private String selectedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);

        radioGroupLocations = findViewById(R.id.radioGroupLocations);
        buttonNext = findViewById(R.id.buttonNext);
        buttonBack = findViewById(R.id.buttonBack);

        addLocationOptions();

        buttonNext.setOnClickListener(v -> {
            int selectedId = radioGroupLocations.getCheckedRadioButtonId();
            if (selectedId != -1) {
                RadioButton selectedRadioButton = findViewById(selectedId);
                selectedLocation = selectedRadioButton.getText().toString();

                Intent intent = new Intent(Activity_SelectLocation.this, Activity_SelectDoctor.class);
                intent.putExtra("selectedLocation", selectedLocation);
                startActivity(intent);
            } else {
                Toast.makeText(Activity_SelectLocation.this, "Please select a location!", Toast.LENGTH_SHORT).show();
            }
        });

        buttonBack.setOnClickListener(v -> onBackPressed());

    }

    private void addLocationOptions() {

        String[] locations = {"425 Broadway Suite 22\nNew York, NY 10018\nClinic 1", "58 Wall Street Suite 100\nNew York, NY 10005\nClinic 2"};

        for (String location : locations) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(location);
            radioGroupLocations.addView(radioButton);
        }

    }

}