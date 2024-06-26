package com.upt.cti.dentalhub.Appointments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.upt.cti.dentalhub.Activity_Login;
import com.upt.cti.dentalhub.Activity_ViewAppointment;
import com.upt.cti.dentalhub.AdminActivity;
import com.upt.cti.dentalhub.Database.DatabaseHelper;
import com.upt.cti.dentalhub.DoctorActivity;
import com.upt.cti.dentalhub.R;
import com.upt.cti.dentalhub.MainActivity;

public class Activity_ConfirmationAppointment extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_appointment);

        mAuth = FirebaseAuth.getInstance();

        TextView textViewConfirmation = findViewById(R.id.textViewConfirmation);
        Button buttonGoToAppointment = findViewById(R.id.buttonGoToAppointment);
        ImageView buttonClose = findViewById(R.id.buttonClose);

        Intent intent = getIntent();
        String doctorName = intent.getStringExtra("selectedDoctor");
        String appointmentDate = intent.getStringExtra("selectedDate");
        String appointmentTime = intent.getStringExtra("selectedTime");
        String firstName = intent.getStringExtra("selectedFirstName");
        String lastName = intent.getStringExtra("selectedLastName");

        String confirmationText = firstName + " " + lastName + " booked an appointment with " + doctorName + " on " + appointmentDate + " at " + appointmentTime + ".";
        textViewConfirmation.setText(confirmationText);

        buttonGoToAppointment.setOnClickListener(v -> checkUserRoleAndRedirect_buttonViewAppointments());

        buttonClose.setOnClickListener(v -> checkUserRoleAndRedirect_buttonClose());

    }

    private void checkUserRoleAndRedirect_buttonViewAppointments() {

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String userId = user.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance("https://dentalhub-1a0c0-default-rtdb.europe-west1.firebasedatabase.app").getReference("users").child(userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        String email = dataSnapshot.child("email").getValue(String.class);
                        if (email != null) {
                            if (isAdmin(email)) {
                                goToAdminActivity();
                            } else if (isDoctor(email)) {
                                goToDoctorActivity();
                            } else {
                                gotToAppointmentsActivity();
                            }
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Activity_ConfirmationAppointment.this, "Failed to retrieve user role", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            goToLogin();
        }

    }

    private void checkUserRoleAndRedirect_buttonClose() {

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String userId = user.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance("https://dentalhub-1a0c0-default-rtdb.europe-west1.firebasedatabase.app").getReference("users").child(userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        String email = dataSnapshot.child("email").getValue(String.class);
                        if (email != null) {
                            if (isAdmin(email)) {
                                goToAdminActivity();
                            } else if (isDoctor(email)) {
                                goToDoctorActivity();
                            } else {
                                goToMainActivity();
                            }
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Activity_ConfirmationAppointment.this, "Failed to retrieve user role", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            goToLogin();
        }

    }

    private boolean isAdmin(String email) {
        return "admin@dentalhub.com".equals(email);
    }

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

    private void goToMainActivity() {

        Intent intent = new Intent(Activity_ConfirmationAppointment.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

    }

    private void gotToAppointmentsActivity() {

        Intent intent = new Intent(Activity_ConfirmationAppointment.this, Activity_ViewAppointment.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

    }

    private void goToAdminActivity() {

        Intent intent = new Intent(Activity_ConfirmationAppointment.this, AdminActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

    }

    private void goToDoctorActivity() {

        Intent intent = new Intent(Activity_ConfirmationAppointment.this, DoctorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

    }

    private void goToLogin() {

        Intent intent = new Intent(this, Activity_Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

    }

}