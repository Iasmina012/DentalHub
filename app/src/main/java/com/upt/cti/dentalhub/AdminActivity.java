package com.upt.cti.dentalhub;

import android.content.Intent;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminActivity extends StaffMenuActivity {

    private static final String TAG = "AdminActivity";

    private RecyclerView recyclerViewAppointments;
    private TextView textViewNoAppointments;
    private DatabaseReference db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private AdminAppointmentAdapter adapter;
    private List<Appointment> appointmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        recyclerViewAppointments = findViewById(R.id.recyclerViewAdmin);
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
        adapter = new AdminAppointmentAdapter(appointmentList, this);
        recyclerViewAppointments.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAppointments.setAdapter(adapter);

        loadAppointments();

    }

    private void loadAppointments() {

        db.addListenerForSingleValueEvent(new ValueEventListener() {
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
            boolean isMissed = intent.getBooleanExtra("isMissed", false);
            String appointmentId = intent.getStringExtra("appointmentId");

            if ((isRescheduled || isReminder || isMissed) && appointmentId != null) {

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
                    String message = appointment.getFullName() + "'s appointment with Dr. " + appointment.getDoctor() +
                            " for " + appointment.getService() + " on " + appointment.getDate() + " at " + appointment.getTime() + " was cancelled.";
                    NotificationHelper notificationHelper = new NotificationHelper(this);
                    notificationHelper.sendCancelNotification("Appointment Cancelled", message);
                })
                .addOnFailureListener(e -> Log.e(TAG, "Failed to cancel appointment", e));

    }

    public void onAppointmentCheckIn(Appointment appointment) {

        new AlertDialog.Builder(this)
                .setTitle("Check In Appointment")
                .setMessage("You are about to check in the patient. Are they present or missing?")
                .setPositiveButton("Present", (dialog, which) -> {
                    moveAppointmentToArchive(appointment, "present");
                })
                .setNegativeButton("Missing", (dialog, which) -> {
                    moveAppointmentToArchive(appointment, "missed");
                })
                .show();

    }

    private void moveAppointmentToArchive(Appointment appointment, String status) {

        DatabaseReference archiveRef = FirebaseDatabase.getInstance("https://dentalhub-1a0c0-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference().child("archived_appointments");

        //map for only relevant fields
        Map<String, Object> appointmentMap = new HashMap<>();
        appointmentMap.put("date", appointment.getDate());
        appointmentMap.put("doctor", appointment.getDoctor());
        appointmentMap.put("firstName", appointment.getFirstName());
        appointmentMap.put("insurance", appointment.getInsurance());
        appointmentMap.put("lastName", appointment.getLastName());
        appointmentMap.put("location", appointment.getLocation());
        appointmentMap.put("service", appointment.getService());
        appointmentMap.put("time", appointment.getTime());
        appointmentMap.put("userId", appointment.getUserId());
        appointmentMap.put("status", status);

        //Firebase
        //move the appointment to the archived_appointments node
        archiveRef.child(appointment.getAppointmentId()).setValue(appointmentMap)
                .addOnSuccessListener(aVoid -> {
                    //remove the appointment from the appointments node
                    db.child(appointment.getAppointmentId()).removeValue()
                            .addOnSuccessListener(aVoid1 -> {
                                appointmentList.remove(appointment);
                                adapter.notifyDataSetChanged();
                                String message = appointment.getFullName() + (status.equals("present") ? " has been checked in for their appointment with Dr. " : " missed their appointment with Dr. ") + appointment.getDoctor();
                                NotificationHelper notificationHelper = new NotificationHelper(this);
                                if (status.equals("present")) {
                                    notificationHelper.sendCheckInNotification("Patient Checked In", message);
                                } else {
                                    notificationHelper.sendMissedNotification("Missed Appointment", message, appointment.getAppointmentId());
                                }
                            })
                            .addOnFailureListener(e -> Log.e(TAG, "Failed to remove appointment from appointments node", e));
                })
                .addOnFailureListener(e -> Log.e(TAG, "Failed to archive appointment", e));

    }

}