package com.upt.cti.dentalhub;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class Activity_Tech extends BaseActivity {

    private List<Device> devices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tech);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewDevice);

        addDevicesOptions();

        DeviceAdapter adapter = new DeviceAdapter(devices);
        recyclerView.setAdapter(adapter);

    }

    private void addDevicesOptions() {

        devices = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_DEVICES, null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DEVICE_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DEVICE_DESCRIPTION));
                int image = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DOCTOR_IMAGE));

                devices.add(new Device(name, description, image));
            }
            cursor.close();
        }

        db.close();

    }

}