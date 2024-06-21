package com.upt.cti.dentalhub;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.upt.cti.dentalhub.Adapters.SymptomAdapter;
import com.upt.cti.dentalhub.Database.DatabaseHelper;
import com.upt.cti.dentalhub.Menus.PromptMenuActivity;
import com.upt.cti.dentalhub.Models.Symptom;

public class Activity_SelectSymptoms extends PromptMenuActivity {

    private RecyclerView recyclerViewSymptoms;
    private Button buttonGetResults;
    private SymptomAdapter symptomAdapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_symptoms);

        dbHelper = new DatabaseHelper(this);
        recyclerViewSymptoms = findViewById(R.id.recyclerViewSymptoms);
        buttonGetResults = findViewById(R.id.buttonGetResults);

        List<Symptom> symptomList = getSymptomsFromDatabase();
        symptomAdapter = new SymptomAdapter(symptomList);
        recyclerViewSymptoms.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSymptoms.setAdapter(symptomAdapter);

        buttonGetResults.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_SelectSymptoms.this, Activity_DiagnosisResult.class);
            String diagnosis = generateDiagnosis();
            if (diagnosis.isEmpty()) {
                Toast.makeText(this, "No symptoms selected or no diagnosis found.", Toast.LENGTH_LONG).show();
            } else {
                intent.putExtra("diagnosis", diagnosis);
                startActivity(intent);
            }
        });

    }

    private List<Symptom> getSymptomsFromDatabase() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Symptom> symptoms = new ArrayList<>();

        Cursor cursor = db.query(DatabaseHelper.TABLE_SYMPTOMS, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_SYMPTOM_NAME);
            do {
                String name = cursor.getString(nameIndex);
                symptoms.add(new Symptom(name));
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return symptoms;

    }

    private String generateDiagnosis() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        StringBuilder diagnosis = new StringBuilder();

        List<Symptom> symptoms = symptomAdapter.getSymptomList();
        List<Long> symptomIds = new ArrayList<>();
        Map<Long, String> symptomMap = new HashMap<>();

        for (Symptom symptom : symptoms) {
            if (symptom.isChecked()) {
                long id = getSymptomId(db, symptom.getName());
                if (id != -1) {
                    symptomIds.add(id);
                    symptomMap.put(id, symptom.getName());
                }
            }
        }

        List<String> diseases = getDiseasesFromSymptoms(db, symptomIds);
        if (!diseases.isEmpty()) {
            diagnosis.append("Possible dental issues:");
            for (String disease : diseases) {
                diagnosis.append("\n- ").append(disease);
            }
        }

        db.close();
        return diagnosis.toString();

    }

    private long getSymptomId(SQLiteDatabase db, String name) {

        Cursor cursor = db.query(DatabaseHelper.TABLE_SYMPTOMS, new String[]{DatabaseHelper.COLUMN_ID},
                DatabaseHelper.COLUMN_SYMPTOM_NAME + "=?", new String[]{name}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
            long id = cursor.getLong(idIndex);
            cursor.close();
            return id;
        }

        return -1;

    }

    private List<String> getDiseasesFromSymptoms(SQLiteDatabase db, List<Long> symptomIds) {

        List<String> diseases = new ArrayList<>();
        if (symptomIds.isEmpty()) return diseases;

        String query = "SELECT DISTINCT " + DatabaseHelper.COLUMN_DISEASE_NAME +
                " FROM " + DatabaseHelper.TABLE_DISEASES + " d, " + DatabaseHelper.TABLE_DISEASE_SYMPTOMS + " ds " +
                "WHERE d." + DatabaseHelper.COLUMN_ID + " = ds." + DatabaseHelper.COLUMN_DISEASE_ID +
                " AND ds." + DatabaseHelper.COLUMN_SYMPTOM_ID + " IN (" + makePlaceholders(symptomIds.size()) + ")";

        Cursor cursor = db.rawQuery(query, toStringArray(symptomIds));
        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DISEASE_NAME);
            do {
                String name = cursor.getString(nameIndex);
                diseases.add(name);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return diseases;

    }

    private String[] toStringArray(List<Long> list) {

        String[] array = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = String.valueOf(list.get(i));
        }

        return array;

    }

    private String makePlaceholders(int len) {

        if (len < 1) {
            throw new RuntimeException("No placeholders");
        } else {
            StringBuilder sb = new StringBuilder(len * 2 - 1);
            sb.append("?");
            for (int i = 1; i < len; i++) {
                sb.append(",?");
            }
            return sb.toString();
        }

    }

}