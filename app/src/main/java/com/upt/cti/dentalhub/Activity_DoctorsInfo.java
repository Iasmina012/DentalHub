package com.upt.cti.dentalhub;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.upt.cti.dentalhub.Adapters.DoctorAdapter;
import com.upt.cti.dentalhub.Appointments.Activity_SelectLocation;
import com.upt.cti.dentalhub.Database.DatabaseHelper;
import com.upt.cti.dentalhub.Menus.MainMenuActivity;
import com.upt.cti.dentalhub.Models.Doctor;

public class Activity_DoctorsInfo extends MainMenuActivity {

    private RecyclerView recyclerView;
    private TextView textViewNoResults;
    private DoctorAdapter doctorAdapter;
    private List<Doctor> doctorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_info);

        doctorList = new ArrayList<>();
        doctorAdapter = new DoctorAdapter(doctorList, this);

        SearchView searchView = findViewById(R.id.searchView);
        textViewNoResults = findViewById(R.id.textViewNoResults);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(doctorAdapter);

        searchView.clearFocus();
        searchView.setQueryHint("Search for a doctor here ...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                filterList(newText);
                return true;

            }
        });

        addDoctorsOptions();

        doctorAdapter.setOnBookNowClickListener(position -> {
            Intent intent = new Intent(Activity_DoctorsInfo.this, Activity_SelectLocation.class);
            startActivity(intent);
        });

    }

    private void addDoctorsOptions() {

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_DOCTORS, null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DOCTOR_NAME));
                int image = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DOCTOR_IMAGE));
                String specialization = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DOCTOR_SPECIALIZATION));
                String phone_number = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DOCTOR_PHONE_NUMBER));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DOCTOR_EMAIL));

                String schedule = getDoctorSchedule(db, cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));

                doctorList.add(new Doctor(image, name, specialization, schedule, phone_number, email));
            }
            cursor.close();
        }

        db.close();
        doctorAdapter.notifyDataSetChanged();

    }

    private String getDoctorSchedule(SQLiteDatabase db, int doctorId) {

        StringBuilder scheduleBuilder = new StringBuilder();
        Cursor cursor = db.query(DatabaseHelper.TABLE_DOCTOR_SCHEDULE,
                null,
                DatabaseHelper.COLUMN_DOCTOR_ID + "=?",
                new String[]{String.valueOf(doctorId)},
                null,
                null,
                null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String dayOfWeek = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DAY_OF_WEEK));
                String startTime = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_START_TIME));
                String endTime = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_END_TIME));

                scheduleBuilder.append(dayOfWeek).append(": ").append(startTime).append(" - ").append(endTime).append("\n");
            }
            cursor.close();
        }

        return scheduleBuilder.toString().trim();

    }

    private void filterList(String text) {

        List<Doctor> filteredList = new ArrayList<>();
        for (Doctor doctor : doctorList) {
            if (doctor.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(doctor);
            }
        }

        if (filteredList.isEmpty()) {
            textViewNoResults.setText(getString(R.string.no_doctors_found));
            textViewNoResults.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textViewNoResults.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            doctorAdapter.setFilteredList(filteredList);
        }

    }

}