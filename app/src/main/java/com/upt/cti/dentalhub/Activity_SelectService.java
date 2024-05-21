package com.upt.cti.dentalhub;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import android.util.Log;

public class Activity_SelectService extends BaseActivity {

    private GridLayout gridLayout;
    private Button buttonNext, buttonBack;
    private Button previouslySelectedButton;
    private String appointmentId;
    private String selectedService;
    private String selectedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_service);

        gridLayout = findViewById(R.id.gridLayoutServices);
        buttonNext = findViewById(R.id.buttonNext);
        buttonBack = findViewById(R.id.buttonBack);

        Intent intent = getIntent();
        appointmentId = intent.getStringExtra("appointmentId");
        selectedLocation = intent.getStringExtra("selectedLocation");
        selectedService = intent.getStringExtra("selectedService");

        if (appointmentId != null) {
            Log.d("Activity_SelectService", "Appointment ID received: " + appointmentId);
        } else {
            Log.e("Activity_SelectService", "Failed to retrieve appointment ID");
        }

        addServiceOptions();

        buttonNext.setOnClickListener(v -> {
            if (selectedService != null) {
                Intent nextIntent = new Intent(Activity_SelectService.this, Activity_SelectDateTime.class);
                nextIntent.putExtra("appointmentId", appointmentId);
                nextIntent.putExtra("selectedLocation", selectedLocation);
                nextIntent.putExtra("selectedDoctor", getIntent().getStringExtra("selectedDoctor"));
                nextIntent.putExtra("selectedService", selectedService);
                nextIntent.putExtra("selectedDate", getIntent().getStringExtra("selectedDate"));
                nextIntent.putExtra("selectedTime", getIntent().getStringExtra("selectedTime"));
                nextIntent.putExtra("selectedInsurance", getIntent().getStringExtra("selectedInsurance"));
                startActivity(nextIntent);
            } else {
                Toast.makeText(Activity_SelectService.this, "Please select a service!", Toast.LENGTH_SHORT).show();
            }
        });

        buttonBack.setOnClickListener(v -> onBackPressed());

    }

    private void addServiceOptions() {

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_SERVICES, null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String serviceName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SERVICE_NAME));

                Button button = new Button(this);
                button.setText(serviceName);
                button.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_bg));
                button.setPadding(16, 16, 16, 16);
                button.setTextColor(ContextCompat.getColor(this, R.color.grey));

                button.setOnClickListener(v -> {
                    if (button == previouslySelectedButton) {
                        deselectButton(button);
                        selectedService = null;
                        previouslySelectedButton = null;
                    } else {
                        selectedService = serviceName;
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

                if (serviceName.equals(selectedService)) {
                    highlightSelectedButton(button);
                }
            }
            cursor.close();
        }

        db.close();

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