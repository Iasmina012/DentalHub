package com.upt.cti.dentalhub;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Activity_ChangePassword extends StaffMenuActivity {

    private EditText editTextNewPassword, editTextConfirmPassword;
    private Button buttonSavePassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonSavePassword = findViewById(R.id.savePasswordButton);

        mAuth = FirebaseAuth.getInstance();

        buttonSavePassword.setOnClickListener(v -> changePassword());

    }

    private void changePassword() {

        String newPassword = editTextNewPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";

        if (TextUtils.isEmpty(newPassword)) {
            editTextNewPassword.setError("Enter a new password!");
            return;
        }

        if (!newPassword.matches(pattern)) {
            editTextNewPassword.setError("The password must contain at least one lowercase letter, one uppercase letter, one numeric digit, one special character (@#$%^&+=) and have a minimum length of 8 characters!");
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            editTextConfirmPassword.setError("Confirm your password!");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            editTextConfirmPassword.setError("The passwords do not match!");
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating Password...");
        progressDialog.show();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.updatePassword(newPassword).addOnCompleteListener(task -> {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(Activity_ChangePassword.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(Activity_ChangePassword.this, "Password update failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            progressDialog.dismiss();
            Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show();
        }

    }

}