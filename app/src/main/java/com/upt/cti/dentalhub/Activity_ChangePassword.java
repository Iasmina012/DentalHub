package com.upt.cti.dentalhub;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.mindrot.jbcrypt.BCrypt;

public class Activity_ChangePassword extends AppCompatActivity {
    private EditText newPassword, confirmNewPassword;
    private Button changePasswordButton;
    private String email, role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        newPassword = findViewById(R.id.editTextNewPassword);
        confirmNewPassword = findViewById(R.id.editTextConfirmPassword);
        changePasswordButton = findViewById(R.id.buttonChangePassword);

        email = getIntent().getStringExtra("email");
        role = getIntent().getStringExtra("role");

        changePasswordButton.setOnClickListener(v -> handleChangePassword());

    }

    private void handleChangePassword() {

        String newPasswordStr = newPassword.getText().toString().trim();
        String confirmNewPasswordStr = confirmNewPassword.getText().toString().trim();

        if (newPasswordStr.isEmpty() || confirmNewPasswordStr.isEmpty()) {
            Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPasswordStr.equals(confirmNewPasswordStr)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null && user.getEmail().equals(email)) {
            user.updatePassword(newPasswordStr).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DatabaseHelper dbHelper = new DatabaseHelper(this);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    ContentValues values = new ContentValues();
                    values.put(DatabaseHelper.COLUMN_DOCTOR_PASSWORD, BCrypt.hashpw(newPasswordStr, BCrypt.gensalt()));

                    db.update(DatabaseHelper.TABLE_DOCTORS, values, DatabaseHelper.COLUMN_DOCTOR_EMAIL + " = ?", new String[]{email});
                    db.close();

                    Toast.makeText(Activity_ChangePassword.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                    goToNextActivity();
                } else {
                    Toast.makeText(Activity_ChangePassword.this, "Failed to change password", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void goToNextActivity() {

        if ("admin".equalsIgnoreCase(role)) {
            goToAdminActivity();
        } else if ("doctor".equalsIgnoreCase(role)) {
            goToDoctorActivity();
        }

    }

    private void goToAdminActivity() {

        Intent intent = new Intent(Activity_ChangePassword.this, AdminActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

    }

    private void goToDoctorActivity() {

        Intent intent = new Intent(Activity_ChangePassword.this, DoctorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

    }

}