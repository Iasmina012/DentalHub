package com.upt.cti.dentalhub;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Activity_Services extends BaseActivity {

    private RecyclerView recyclerView;
    private TextView textViewNoResults;
    private ServicesAdapter servicesAdapter;
    private List<Services> servicesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        servicesList = new ArrayList<>();
        servicesAdapter = new ServicesAdapter(servicesList);

        SearchView searchView = findViewById(R.id.searchView);
        textViewNoResults = findViewById(R.id.textViewNoResults);
        recyclerView = findViewById(R.id.servicesRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(servicesAdapter);

        searchView.clearFocus();
        searchView.setQueryHint("Search for a service here ...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {

                filterList(newText);
                return true;

            }
        });

        addServicesOptions();

        Button buttonBookNow = findViewById(R.id.buttonBook);
        buttonBookNow.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_Services.this, Activity_SelectLocation.class);
            startActivity(intent);
        });

    }

    private void addServicesOptions() {

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_SERVICES, null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SERVICE_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SERVICE_DESCRIPTION));
                int image = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SERVICE_IMAGE));

                servicesList.add(new Services(name, description, image));
            }

            cursor.close();
        }

        db.close();
        servicesAdapter.notifyDataSetChanged();

    }

    private void filterList(String text) {

        List<Services> filteredList = new ArrayList<>();
        for (Services service : servicesList) {
            if (service.getTitle().toLowerCase().contains(text.toLowerCase()) ||
                    service.getDescription().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(service);
            }
        }

        if (filteredList.isEmpty()) {
            textViewNoResults.setText(getString(R.string.no_services_found));
            textViewNoResults.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textViewNoResults.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            servicesAdapter.setFilteredList(filteredList);
        }

    }

}