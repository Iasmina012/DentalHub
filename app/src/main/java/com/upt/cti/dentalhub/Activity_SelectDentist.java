package com.upt.cti.dentalhub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import androidx.appcompat.app.AppCompatActivity;

public class Activity_SelectDentist extends AppCompatActivity {

    private RadioGroup radioGroupDentists;
    private Button buttonNext, buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_dentist);

        radioGroupDentists = findViewById(R.id.radioGroupDentists);
        buttonNext = findViewById(R.id.buttonNext);
        buttonBack = findViewById(R.id.buttonBack);

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

                Intent intent = new Intent(Activity_SelectDentist.this, Activity_SelectService.class);
                intent.putExtra("selectedDentist", selectedDentist);
                startActivity(intent);
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