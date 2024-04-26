package com.upt.cti.dentalhub;

import android.os.Bundle;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Activity_DoctorsInfo extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SearchView searchView;
    private DoctorAdapter doctorAdapter;
    private List<Doctor> doctorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_info);

        doctorList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        doctorAdapter = new DoctorAdapter(doctorList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(doctorAdapter);

        addDoctorsToList();

    }

    private void addDoctorsToList() {

        doctorList.add(new Doctor(R.drawable.item1,"Dr. John Doe", "Stomatolog", "Luni - Vineri, 08:00 - 16:00","07403030303030", "akm@yahoo.com"));
        doctorList.add(new Doctor(R.drawable.item1,"Dr. John Doe", "Stomatolog", "Luni - Vineri, 08:00 - 16:00","07403030303030", "akm@yahoo.com"));
        doctorList.add(new Doctor(R.drawable.item1,"Dr. John Doe", "Stomatolog", "Luni - Vineri, 08:00 - 16:00","07403030303030", "akm@yahoo.com"));
        doctorList.add(new Doctor(R.drawable.item1,"Dr. John Doe", "Stomatolog", "Luni - Vineri, 08:00 - 16:00","07403030303030", "akm@yahoo.com"));
        doctorList.add(new Doctor(R.drawable.item1,"Dr. John Doe", "Stomatolog", "Luni - Vineri, 08:00 - 16:00","07403030303030", "akm@yahoo.com"));

        doctorAdapter.notifyDataSetChanged();

    }

}
