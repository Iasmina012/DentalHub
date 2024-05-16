package com.upt.cti.dentalhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Activity_SelectService extends AppCompatActivity {

    private Button buttonNext, buttonBack;
    private String selectedService;
    private String selectedLocation;
    private GridLayout gridLayout;
    private Button previouslySelectedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_service);

        buttonNext = findViewById(R.id.buttonNext);
        buttonBack = findViewById(R.id.buttonBack);
        gridLayout = findViewById(R.id.gridLayoutServices);

        selectedLocation = getIntent().getStringExtra("selectedLocation");

        addServiceButtons();

        buttonNext.setOnClickListener(v -> {
            if (selectedService != null) {
                Intent intent = new Intent(Activity_SelectService.this, Activity_SelectDateTime.class);
                intent.putExtra("selectedService", selectedService);
                intent.putExtra("selectedDentist", getIntent().getStringExtra("selectedDentist"));
                intent.putExtra("selectedLocation", selectedLocation);
                startActivity(intent);
            } else {
                Toast.makeText(Activity_SelectService.this, "Please select a service!", Toast.LENGTH_SHORT).show();
            }
        });

        buttonBack.setOnClickListener(v -> onBackPressed());

    }

    private void addServiceButtons() {

        String[] services = {
                "Specialized Consultation",
                "Dental Prophylaxis",
                "Dental Aesthetics",
                "Orthodontics And Dento-facial Orthopedics",
                "Pedodontics (Pediatric Dentistry)",
                "Odontotherapy",
                "Endodontics",
                "Periodontology",
                "Dental Prosthetics",
                "Dental Surgery",
                "Implantology",
                "Dental Radiology"
        };

        for (String service : services) {

            Button button = new Button(this);
            button.setText(service);
            button.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_bg));
            button.setPadding(16, 16, 16, 16);
            button.setTextColor(ContextCompat.getColor(this, R.color.grey));

            button.setOnClickListener(v -> {
                if (button == previouslySelectedButton) {
                    deselectButton(button);
                    selectedService = null;
                    previouslySelectedButton = null;
                } else {
                    selectedService = service;
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

        }

    }

    private void highlightSelectedButton(Button selectedButton) {

        if (previouslySelectedButton != null) {
            deselectButton(previouslySelectedButton);
        }

        selectedButton.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_bg_selected));
        selectedButton.setTextColor(ContextCompat.getColor(this, R.color.white));

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.scale);
        selectedButton.startAnimation(anim);

        previouslySelectedButton = selectedButton;

    }

    private void deselectButton(Button button) {

        button.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_bg));
        button.setTextColor(ContextCompat.getColor(this, R.color.grey));

        Animation reverseAnim = AnimationUtils.loadAnimation(this, R.anim.scale_reverse);
        button.startAnimation(reverseAnim);

    }

}