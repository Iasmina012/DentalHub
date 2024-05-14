package com.upt.cti.dentalhub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Activity_SelectService extends AppCompatActivity {

    private Button buttonNext, buttonBack;
    private String selectedService;
    private GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_service);

        buttonNext = findViewById(R.id.buttonNext);
        buttonBack = findViewById(R.id.buttonBack);
        gridLayout = findViewById(R.id.gridLayoutServices);

        addServiceButtons();

        buttonNext.setOnClickListener(v -> {
            if (selectedService != null) {
                Intent intent = new Intent(Activity_SelectService.this, Activity_SelectDateTime.class);
                intent.putExtra("selectedService", selectedService);
                intent.putExtra("selectedDentist", getIntent().getStringExtra("selectedDentist"));
                startActivity(intent);
            } else {
                Toast.makeText(Activity_SelectService.this, "Please select a service", Toast.LENGTH_SHORT).show();
            }
        });

        buttonBack.setOnClickListener(v -> onBackPressed());

    }

    private void addServiceButtons() {

        String[] services = {
                "Specialized\nConsultation",
                "Dental\nProphylaxis",
                "Dental\nAesthetics",
                "Orthodontics And\nDento-facial Orthopedics",
                "Pedodontics\n(Pediatric Dentistry)",
                "Odontotherapy",
                "Endodontics",
                "Periodontology",
                "Dental\nProsthetics",
                "Dental\nSurgery",
                "Implantology",
                "Dental\nRadiology"
        };

        for (String service : services) {
            Button button = new Button(this);
            button.setText(service);
            button.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_bg));
            button.setPadding(16, 16, 16, 16);
            button.setOnClickListener(v -> selectedService = service);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
            params.setMargins(8, 8, 8, 8);
            button.setLayoutParams(params);

            gridLayout.addView(button);
        }

    }

}