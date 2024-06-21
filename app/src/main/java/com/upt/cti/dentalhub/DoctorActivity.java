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

import com.upt.cti.dentalhub.Adapters.DoctorAppointmentAdapter;
import com.upt.cti.dentalhub.Appointments.Activity_SelectLocation;
import com.upt.cti.dentalhub.Database.DatabaseHelper;
import com.upt.cti.dentalhub.Menus.StaffMenuActivity;
import com.upt.cti.dentalhub.Models.Appointment;
import com.upt.cti.dentalhub.Notifications.NotificationHelper;

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
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(TAG, "Database error: " + databaseError.getMessage());
                    }
                });

    }

    private void fetchPatientName(Appointment appointment) {

        String userId = appointment.getUserId();
        if (userId == null) {
            appointment.setUserName("Unknown Patient");
            appointmentList.add(appointment);
            updateUI();
            return;
        }

        DatabaseReference usersRef = FirebaseDatabase.getInstance("https://dentalhub-1a0c0-default-rtdb.europe-west1.firebasedatabase.app").getReference("users").child(userId);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    String firstName = dataSnapshot.child("firstName").getValue(String.class);
                    String lastName = dataSnapshot.child("lastName").getValue(String.class);
                    if (firstName != null && lastName != null) {
                        appointment.setUserName(firstName + " " + lastName);
                    } else {
                        appointment.setUserName("Unknown Patient");
                    }
                } else {
                    appointment.setUserName("Unknown Patient");
                }

                appointmentList.add(appointment);
                updateUI();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
        } else {
            textViewNoAppointments.setVisibility(View.GONE);
            recyclerViewAppointments.setVisibility(View.VISIBLE);
            handleIntent(getIntent());
            adapter.notifyDataSetChanged();
        }

    }

    private void handleIntent(Intent intent) {

        if (intent != null) {

            boolean isRescheduled = intent.getBooleanExtra("isRescheduled", false);
            boolean isReminder = intent.getBooleanExtra("isReminder", false);
            String appointmentId = intent.getStringExtra("appointmentId");

            if ((isRescheduled || isReminder) && appointmentId != null) {

                recyclerViewAppointments.post(() -> {
                    for (int i = 0; i < appointmentList.size(); i++) {
                        if (appointmentList.get(i).getAppointmentId().equals(appointmentId)) {
                            Appointment rescheduledAppointment = appointmentList.remove(i);
                            appointmentList.add(0, rescheduledAppointment);
                            adapter.notifyDataSetChanged();
                            recyclerViewAppointments.scrollToPosition(0);
                            break;
                        }
                    }
                });

            }

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

        db.child(appointment.getAppointmentId()).removeValue()
                .addOnSuccessListener(aVoid -> {
                    appointmentList.remove(appointment);
                    adapter.notifyDataSetChanged();
                    String message = appointment.getFirstName() + " " + appointment.getLastName() + "'s appointment with " + appointment.getDoctor() +
                            " for " + appointment.getService() + " on " + appointment.getDate() + " at " + appointment.getTime() + " was cancelled.";
                    NotificationHelper notificationHelper = new NotificationHelper(this);
                    notificationHelper.sendCancelNotification("Appointment Cancelled", message);
                })
                .addOnFailureListener(e -> Log.e(TAG, "Failed to cancel appointment", e));

    }

}