package com.upt.cti.dentalhub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Activity_DiagnosisResult extends BaseActivity {

    private TextView textViewDiagnosis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis_result);

        textViewDiagnosis = findViewById(R.id.textViewDiagnosis);
        ImageView buttonClose = findViewById(R.id.buttonClose);

        String diagnosis = getIntent().getStringExtra("diagnosis");
        textViewDiagnosis.setText(diagnosis);

        buttonClose.setOnClickListener(v -> {
            Intent mainIntent = new Intent(Activity_DiagnosisResult.this, MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mainIntent);
            finish();
        });

    }

}