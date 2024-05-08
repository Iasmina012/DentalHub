package com.upt.cti.dentalhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
//import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Activity_DoctorsInfo extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DoctorAdapter doctorAdapter;
    private List<Doctor> doctorList;
    private TextView textViewNoResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_info);

        doctorList = new ArrayList<>();
        doctorAdapter = new DoctorAdapter(doctorList);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(doctorAdapter);

        textViewNoResults = findViewById(R.id.textViewNoResults);
        SearchView searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setQueryHint("Search for a doctor here ...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

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

        addDoctorsToList();

        doctorAdapter.setOnBookNowClickListener(position -> {
            Intent intent = new Intent(Activity_DoctorsInfo.this, Activity_Appointment.class);
            startActivity(intent);
        });

    }

    private void addDoctorsToList() {

        doctorList.add(new Doctor(R.drawable.item1, "Dr. Daniela Pop", "Endodontist/Periodontist", "Monday - Friday, 08:00 AM - 04:00 PM", "0721122334", "daniela@gmail.com"));
        doctorList.add(new Doctor(R.drawable.item1, "Dr. Ana Maria Popescu", "Orthodontist", "Monday - Friday, 09:30 AM - 05:30 PM", "0723456789", "ana@yahoo.com"));
        doctorList.add(new Doctor(R.drawable.item1, "Dr. Maria Ionescu", "Implantologist", "Monday - Friday, 10:30 AM - 06:30 PM", "0734567890", "maria@yahoo.com"));
        doctorList.add(new Doctor(R.drawable.item1, "Dr. Andrei Radu", "Pedodontist", "Monday - Friday, 08:30 AM - 04:30 PM", "0745678901", "andrei@gmail.com"));
        doctorList.add(new Doctor(R.drawable.item1, "Dr. Elena Popa", "Prosthodontist", "Monday - Friday, 08:00 AM - 04:00 PM", "0756789012", "elena@gmail.com"));

        doctorAdapter.notifyDataSetChanged();

    }

    private void filterList(String text) {

        List<Doctor> filteredList = new ArrayList<>();
        for(Doctor doctor : doctorList) {

            if (doctor.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(doctor);
            }
        }

        if(filteredList.isEmpty()){
            //Toast.makeText(this, "No doctors found!", Toast.LENGTH_SHORT).show();
            textViewNoResults.setText("No doctors found!");
            textViewNoResults.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textViewNoResults.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            doctorAdapter.setFilteredList(filteredList);
        }

    }

}