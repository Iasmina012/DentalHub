package com.upt.cti.dentalhub;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Activity_Services extends BaseActivity {

    private List<Services> servicesItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        RecyclerView recyclerView = findViewById(R.id.servicesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        servicesItems = new ArrayList<>();
        addServicesOptions();

        ServicesAdapter adapter = new ServicesAdapter(servicesItems);
        recyclerView.setAdapter(adapter);

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

                servicesItems.add(new Services(name, description, image));
            }
            cursor.close();
        }

        db.close();

    }

}