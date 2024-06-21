package com.upt.cti.dentalhub;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Activity_SelectInsurance extends PromptMenuActivity {

    private static final String TAG = "Activity_SelectInsurance";

    private RadioGroup radioGroupInsurance;
    private Button buttonBook, buttonBack;
    private String appointmentId;
    private String selectedLocation;
    private String selectedDoctor;
    private int selectedDoctorId;
    private String selectedService;
    private String selectedDate;
    private String selectedTime;
    private String selectedInsurance;
    private String selectedFirstName;
    private String selectedLastName;
    private String userId;
    private String oldDate;
    private String oldTime;
    private DatabaseReference db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_insurance);

        radioGroupInsurance = findViewById(R.id.radioGroupInsurance);
        buttonBook = findViewById(R.id.buttonBook);
        buttonBack = findViewById(R.id.buttonBack);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://dentalhub-1a0c0-default-rtdb.europe-west1.firebasedatabase.app");
        db = database.getReference();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "User is not authenticated!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Intent intent = getIntent();
        appointmentId = intent.getStringExtra("appointmentId");
        selectedLocation = intent.getStringExtra("selectedLocation");
        selectedDoctor = intent.getStringExtra("selectedDoctor");
        selectedDoctorId = intent.getIntExtra("selectedDoctorId", -1);
        selectedService = intent.getStringExtra("selectedService");
        selectedDate = intent.getStringExtra("selectedDate");
        selectedTime = intent.getStringExtra("selectedTime");
        selectedInsurance = intent.getStringExtra("selectedInsurance");
        selectedFirstName = intent.getStringExtra("selectedFirstName");
        selectedLastName = intent.getStringExtra("selectedLastName");
        userId = intent.getStringExtra("userId");

        if (appointmentId != null) {
            oldDate = intent.getStringExtra("oldDate");
            oldTime = intent.getStringExtra("oldTime");
        }

        addInsurancesOptions();

        for (int i = 0; i < radioGroupInsurance.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) radioGroupInsurance.getChildAt(i);
            radioButton.setTextSize(20);

            if (radioButton.getText().toString().equals(selectedInsurance)) {
                radioButton.setChecked(true);
            }
        }

        buttonBook.setOnClickListener(v -> {
            int selectedId = radioGroupInsurance.getCheckedRadioButtonId();
            if (selectedId != -1) {
                RadioButton selectedRadioButton = findViewById(selectedId);
                selectedInsurance = selectedRadioButton.getText().toString();

                Log.d(TAG, "Insurance selected: " + selectedInsurance);
                bookAppointment();
            } else {
                Toast.makeText(Activity_SelectInsurance.this, "Please select an insurance type!", Toast.LENGTH_SHORT).show();
            }
        });

        buttonBack.setOnClickListener(v -> onBackPressed());

    }

    private void addInsurancesOptions() {

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_INSURANCES, null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String insuranceName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_INSURANCE_NAME));

                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(insuranceName);
                radioButton.setTextSize(20);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 0, 18);
                radioButton.setLayoutParams(params);

                radioGroupInsurance.addView(radioButton);
            }
            cursor.close();
        }

        db.close();

    }

    private void bookAppointment() {

        boolean isReschedule = appointmentId != null;

        if (appointmentId == null) {
            //generate new appointment ID
            appointmentId = db.child("appointments").push().getKey();
            if (appointmentId == null) {
                Toast.makeText(this, "Failed to generate appointment ID!", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Failed to generate appointment ID");
                return;
            }
        }

        String currentUserId = currentUser.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance("https://dentalhub-1a0c0-default-rtdb.europe-west1.firebasedatabase.app").getReference("users").child(currentUserId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    String email = dataSnapshot.child("email").getValue(String.class);
                    boolean isAdmin = isAdmin(email);
                    boolean isDoctor = isDoctor(email);

                    String userIdToUse = userId;
                    if (!isAdmin && !isDoctor) {
                        userIdToUse = currentUserId; //if the current user is a client, use their userId
                    } else if (userId == null || userId.isEmpty()) {
                        //set userId only if it's not already set
                        userIdToUse = currentUserId;
                    }

                    Map<String, Object> appointment = new HashMap<>();
                    appointment.put("location", selectedLocation);
                    appointment.put("doctor", selectedDoctor);
                    appointment.put("service", selectedService);
                    appointment.put("date", selectedDate);
                    appointment.put("time", selectedTime);
                    appointment.put("insurance", selectedInsurance);
                    appointment.put("firstName", selectedFirstName);
                    appointment.put("lastName", selectedLastName);
                    appointment.put("userId", userIdToUse); //appropriate userId

                    //Firebase
                    db.child("appointments").child(appointmentId).setValue(appointment)
                            .addOnSuccessListener(aVoid -> {
                                //SQLite
                                addAppointmentToSQLite();

                                Toast.makeText(Activity_SelectInsurance.this, "Appointment booked successfully!", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "Appointment booked successfully");

                                //send reschedule notification if it was a reschedule
                                if (isReschedule) {
                                    String message = selectedFirstName + " " + selectedLastName + "'s appointment with " + selectedDoctor +
                                            " for " + selectedService + " was modified.";
                                    NotificationHelper notificationHelper = new NotificationHelper(Activity_SelectInsurance.this);
                                    notificationHelper.sendRescheduleNotification("Appointment Rescheduled", message, appointmentId);
                                }

                                //schedule the 24 hours reminder
                                scheduleReminder(selectedDate, selectedTime, appointmentId, selectedFirstName, selectedDoctor, selectedService);

                                Intent intent = new Intent(Activity_SelectInsurance.this, Activity_ConfirmationAppointment.class);
                                intent.putExtra("selectedDoctor", selectedDoctor);
                                intent.putExtra("selectedDate", selectedDate);
                                intent.putExtra("selectedTime", selectedTime);
                                intent.putExtra("selectedLocation", selectedLocation);
                                intent.putExtra("selectedFirstName", selectedFirstName);
                                intent.putExtra("selectedLastName", selectedLastName);
                                startActivity(intent);
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(Activity_SelectInsurance.this, "Failed to book appointment!", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "Failed to book appointment", e);
                            });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Activity_SelectInsurance.this, "Failed to retrieve user role", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addAppointmentToSQLite() {

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_DOCTOR_ID, getDoctorIdByName(selectedDoctor, db));
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_DATE, selectedDate);
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_TIME, selectedTime);
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_INSURANCE, selectedInsurance);
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_FIRST_NAME, selectedFirstName);
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_LAST_NAME, selectedLastName);
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_USER_ID, userId);
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_LOCATION, selectedLocation);
        values.put(DatabaseHelper.COLUMN_APPOINTMENT_SERVICE, selectedService);

        long newRowId = db.insert(DatabaseHelper.TABLE_APPOINTMENTS, null, values);
        if (newRowId == -1) {
            Log.e(TAG, "Failed to add appointment to SQLite");
        } else {
            Log.d(TAG, "Appointment added to SQLite with ID: " + newRowId);
        }

        db.close();

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

    private boolean isAdmin(String email) { return "admin@dentalhub.com".equals(email); }

    private boolean isDoctor(String email) {

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_DOCTORS + " WHERE " + DatabaseHelper.COLUMN_DOCTOR_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        boolean isDoctor = false;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                isDoctor = cursor.getInt(0) > 0;
            }
            cursor.close();
        }

        db.close();
        return isDoctor;

    }

    private void scheduleReminder(String date, String time, String appointmentId, String firstName, String doctor, String service) {

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        try {
            Date appointmentDate = dateTimeFormat.parse(date + " " + time);
            if (appointmentDate != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(appointmentDate);
                calendar.add(Calendar.DAY_OF_YEAR, -1); // 24 hours before

                Log.d("Reminder", "Reminder set for: " + calendar.getTime());

                Intent intent = new Intent(this, NotificationReceiver.class);
                intent.putExtra("title", "Appointment Reminder");
                intent.putExtra("message", firstName + "'s appointment with Dr. " + doctor + " for " + service + " is tomorrow.");
                intent.putExtra("appointmentId", appointmentId);
                intent.putExtra("isReminder", true);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, appointmentId.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                if (alarmManager != null) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    Log.d("Reminder", "Alarm set for: " + calendar.getTime());
                }
            }
        } catch (ParseException e) {
        Log.e("Reminder", "Failed to parse date and time: " + date + " " + time, e);
        }

    }

}
