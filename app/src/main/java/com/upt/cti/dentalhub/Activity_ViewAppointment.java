package com.upt.cti.dentalhub;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Activity_ViewAppointment extends MainMenuActivity {

    private static final String TAG = "ViewAppointments";

    private RecyclerView recyclerViewAppointments;
    private TextView textViewNoAppointments;
    private DatabaseReference db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private AppointmentAdapter adapter;
    private List<Appointment> appointmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointment);

        recyclerViewAppointments = findViewById(R.id.recyclerViewAppointments);
        textViewNoAppointments = findViewById(R.id.textViewNoAppointments);

        db = FirebaseDatabase.getInstance("https://dentalhub-1a0c0-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("appointments");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Log.e(TAG, "User not authenticated!");
            finish();
            return;
        }

        appointmentList = new ArrayList<>();
        adapter = new AppointmentAdapter(appointmentList, this);
        recyclerViewAppointments.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAppointments.setAdapter(adapter);

        loadAppointments();

    }

    private void loadAppointments() {

        db.orderByChild("userId").equalTo(currentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        appointmentList.clear();

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Appointment currentAppointment = snapshot.getValue(Appointment.class);
                                if (currentAppointment != null) {
                                    currentAppointment.setAppointmentId(snapshot.getKey());
                                    appointmentList.add(currentAppointment);
                                    Log.d(TAG, "Appointment added: " + currentAppointment);
                                }
                            }
                        } else {
                            Log.d(TAG, "No appointments found for user: " + currentUser.getUid());
                        }

                        if (appointmentList.isEmpty()) {
                            textViewNoAppointments.setVisibility(View.VISIBLE);
                            recyclerViewAppointments.setVisibility(View.GONE);
                            Log.d(TAG, "No appointments available");
                        } else {
                            textViewNoAppointments.setVisibility(View.GONE);
                            recyclerViewAppointments.setVisibility(View.VISIBLE);
                            adapter.notifyDataSetChanged();
                            Log.d(TAG, "Appointments loaded, count: " + appointmentList.size());
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { Log.e(TAG, "Database error: " + databaseError.getMessage()); }
                });

    }

    public void onAppointmentReschedule(Appointment appointment) {

        Intent intent = new Intent(this, Activity_SelectLocation.class);
        intent.putExtra("appointmentId", appointment.getAppointmentId());
        intent.putExtra("selectedLocation", appointment.getLocation());
        intent.putExtra("selectedDoctor", appointment.getDoctor());
        intent.putExtra("selectedService", appointment.getService());
        intent.putExtra("selectedDate", appointment.getDate());
        intent.putExtra("selectedTime", appointment.getTime());
        intent.putExtra("selectedInsurance", appointment.getInsurance());
        intent.putExtra("selectedFirstName", appointment.getFirstName());
        intent.putExtra("selectedLastName", appointment.getLastName());
        intent.putExtra("userId", appointment.getUserId());
        startActivity(intent);

    }

    public void onAppointmentCancel(Appointment appointment) {

        new AlertDialog.Builder(this)
                .setTitle("Cancel Appointment")
                .setMessage("Are you sure you want to cancel this appointment?")
                .setPositiveButton("Yes", (dialog, which) -> cancelAppointment(appointment))
                .setNegativeButton("No", null)
                .show();

    }

    private void cancelAppointment(Appointment appointment) {

        //Firebase
        db.child(appointment.getAppointmentId()).removeValue().addOnSuccessListener(aVoid -> {

                    //SQLite
                    DatabaseHelper dbHelper = new DatabaseHelper(this);
                    SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
                    sqLiteDatabase.delete(DatabaseHelper.TABLE_APPOINTMENTS,
                            DatabaseHelper.COLUMN_APPOINTMENT_DOCTOR_ID + "=? AND " +
                                    DatabaseHelper.COLUMN_APPOINTMENT_DATE + "=? AND " +
                                    DatabaseHelper.COLUMN_APPOINTMENT_TIME + "=?",
                            new String[]{String.valueOf(getDoctorIdByName(appointment.getDoctor(), sqLiteDatabase)), appointment.getDate(), appointment.getTime()});
                    sqLiteDatabase.close();

                    appointmentList.remove(appointment);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(Activity_ViewAppointment.this, "Appointment canceled successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Activity_ViewAppointment.this, "Failed to cancel appointment", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Failed to cancel appointment", e);
                });
    }

    private int getDoctorIdByName(String doctorName, SQLiteDatabase db) {

        Cursor cursor = db.query(DatabaseHelper.TABLE_DOCTORS,
                new String[]{DatabaseHelper.COLUMN_ID},
                DatabaseHelper.COLUMN_DOCTOR_NAME + "=?",
                new String[]{doctorName},
                null,
                null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            int doctorId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
            cursor.close();
            return doctorId;
        }

        return -1; //cand un doctor nu e gasit

    }

}