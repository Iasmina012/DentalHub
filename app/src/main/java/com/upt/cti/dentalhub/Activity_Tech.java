package com.upt.cti.dentalhub;

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

public class Activity_Tech extends MainMenuActivity {

    private RecyclerView recyclerView;
    private TextView textViewNoResults;
    private DeviceAdapter deviceAdapter;
    private List<Device> deviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tech);

        deviceList = new ArrayList<>();
        deviceAdapter = new DeviceAdapter(deviceList);

        SearchView searchView = findViewById(R.id.searchView);
        textViewNoResults = findViewById(R.id.textViewNoResults);
        recyclerView = findViewById(R.id.recyclerViewDevice);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(deviceAdapter);

        searchView.clearFocus();
        searchView.setQueryHint("Search for a technology here ...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {

                filterList(newText);
                return true;

            }
        });

        addDevicesOptions();

    }

    private void addDevicesOptions() {

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_DEVICES, null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DEVICE_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DEVICE_DESCRIPTION));
                int image = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DOCTOR_IMAGE));

                deviceList.add(new Device(name, description, image));
            }
            cursor.close();
        }

        db.close();
        deviceAdapter.notifyDataSetChanged();

    }

    private void filterList(String text) {

        List<Device> filteredList = new ArrayList<>();
        for (Device device : deviceList) {
            if (device.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(device);
            }
        }

        if (filteredList.isEmpty()) {
            textViewNoResults.setText(getString(R.string.no_devices_found));
            textViewNoResults.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textViewNoResults.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            deviceAdapter.setFilteredList(filteredList);
        }

    }

}