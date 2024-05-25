package com.upt.cti.dentalhub;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

public class DoctorActivity extends StaffMenuActivity {

    private static final String TAG = "DoctorActivity";

    private RecyclerView recyclerViewAppointments;
    private TextView textViewNoAppointments;
    private DatabaseReference db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DoctorAppointmentAdapter adapter;
    private List<Appointment> appointmentList;
    private String doctorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        recyclerViewAppointments = findViewById(R.id.recyclerViewDoctor);
        textViewNoAppointments = findViewById(R.id.textViewNoAppointments);

        db = FirebaseDatabase.getInstance("https://dentalhub-1a0c0-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("appointments");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Log.e(TAG, "User not authenticated!");
            finish();
            return;
        }

        doctorName = getDoctorName(currentUser.getEmail());

        appointmentList = new ArrayList<>();
        adapter = new DoctorAppointmentAdapter(appointmentList, this);
        recyclerViewAppointments.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAppointments.setAdapter(adapter);

        loadAppointments();

    }

    private String getDoctorName(String email) {

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String doctorName = "";

        String query = "SELECT " + DatabaseHelper.COLUMN_DOCTOR_NAME + " FROM " + DatabaseHelper.TABLE_DOCTORS + " WHERE " + DatabaseHelper.COLUMN_DOCTOR_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                doctorName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DOCTOR_NAME));
            }
            cursor.close();
        }

        db.close();
        return doctorName;

    }

    private void loadAppointments() {

        db.orderByChild("doctor").equalTo(doctorName)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        appointmentList.clear();

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Appointment currentAppointment = snapshot.getValue(Appointment.class);
                                if (currentAppointment != null) {
                                    currentAppointment.setAppointmentId(snapshot.getKey());
                                    fetchPatientName(currentAppointment);
                                }
                            }
                        } else {
                            updateUI();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { Log.e(TAG, "Database error: " + databaseError.getMessage()); }

                });

    }

    private void fetchPatientName(Appointment appointment) {

        DatabaseReference usersRef = FirebaseDatabase.getInstance("https://dentalhub-1a0c0-default-rtdb.europe-west1.firebasedatabase.app").getReference("users");
        usersRef.child(appointment.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    String patientName = dataSnapshot.child("displayName").getValue(String.class);
                    appointment.setUserName(patientName);
                } else {
                    appointment.setUserName("Unknown Patient");
                }

                appointmentList.add(appointment);
                updateUI();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.e(TAG, "Failed to fetch patient name: " + databaseError.getMessage());
                appointment.setUserName("Unknown Patient");
                appointmentList.add(appointment);
                updateUI();

            }

        });

    }

    private void updateUI() {

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

    public void onAppointmentReschedule(Appointment appointment) {

        Intent intent = new Intent(this, Activity_SelectLocation.class);
        intent.putExtra("appointmentId", appointment.getAppointmentId());
        intent.putExtra("selectedLocation", appointment.getLocation());
        intent.putExtra("selectedDoctor", appointment.getDoctor());
        intent.putExtra("selectedService", appointment.getService());
        intent.putExtra("selectedDate", appointment.getDate());
        intent.putExtra("selectedTime", appointment.getTime());
        intent.putExtra("selectedInsurance", appointment.getInsurance());
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

        db.child(appointment.getAppointmentId()).removeValue()
                .addOnSuccessListener(aVoid -> {
                    appointmentList.remove(appointment);
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "Appointment canceled successfully");
                })
                .addOnFailureListener(e -> Log.e(TAG, "Failed to cancel appointment", e));

    }

}