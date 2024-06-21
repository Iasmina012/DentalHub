package com.upt.cti.dentalhub;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.PorterDuff;

import androidx.core.content.ContextCompat;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.bumptech.glide.Glide;

public class Activity_MyAccount extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView profileImage;
    private TextView textFirstName, textLastName, textUsername, textEmail;
    private EditText editTextFirstName, editTextLastName, editTextUsername, editTextEmail, editTextNewPassword, editTextConfirmPassword;
    private Button buttonEdit, buttonSave, buttonAppointments, buttonChooseFile, buttonChangePassword, buttonRemovePicture;
    private ImageButton buttonClose;
    private Uri imageUri;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;
    private FirebaseUser currentUser;
    private boolean isDefaultImage = true;
    private boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        profileImage = findViewById(R.id.profile_image);
        textFirstName = findViewById(R.id.text_first_name);
        textLastName = findViewById(R.id.text_last_name);
        textUsername = findViewById(R.id.text_username);
        textEmail = findViewById(R.id.text_email);
        editTextFirstName = findViewById(R.id.edit_text_first_name);
        editTextLastName = findViewById(R.id.edit_text_last_name);
        editTextUsername = findViewById(R.id.edit_text_username);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextNewPassword = findViewById(R.id.edit_text_new_password);
        editTextConfirmPassword = findViewById(R.id.edit_text_confirm_password);
        buttonEdit = findViewById(R.id.button_edit);
        buttonSave = findViewById(R.id.button_save);
        buttonAppointments = findViewById(R.id.button_appointments);
        buttonChooseFile = findViewById(R.id.button_choose_file);
        buttonChangePassword = findViewById(R.id.button_change_password);
        buttonRemovePicture = findViewById(R.id.button_remove_file);
        buttonClose = findViewById(R.id.buttonClose);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance("https://dentalhub-1a0c0-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users").child(currentUser.getUid());
        mStorageRef = FirebaseStorage.getInstance("gs://dentalhub-1a0c0.appspot.com").getReference("profile_images");

        setDefaultProfileImage();

        loadUserProfile();

        buttonEdit.setOnClickListener(v -> {
            enableEditing(true);
            resetPasswordFields();
        });

        buttonSave.setOnClickListener(v -> saveUserProfile());

        buttonChooseFile.setOnClickListener(v -> openFileChooser());

        buttonRemovePicture.setOnClickListener(v -> removeProfilePicture());

        buttonAppointments.setOnClickListener(v -> checkUserRoleAndRedirect());

        buttonChangePassword.setOnClickListener(v -> showPasswordFields(true));

        buttonClose.setOnClickListener(v -> {
            if (isEditing) {
                showDiscardChangesDialog();
            } else {
                onBackPressed();
            }
        });

    }

    private void setDefaultProfileImage() {

        profileImage.setImageResource(R.drawable.username_icon);
        profileImage.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(this, R.color.lightTeal), PorterDuff.Mode.SRC_IN));
        isDefaultImage = true;
        buttonRemovePicture.setVisibility(View.GONE);

    }

    private void enableEditing(boolean enable) {

        isEditing = enable;

        textFirstName.setVisibility(enable ? View.GONE : View.VISIBLE);
        textLastName.setVisibility(enable ? View.GONE : View.VISIBLE);
        textUsername.setVisibility(enable ? View.GONE : View.VISIBLE);
        textEmail.setVisibility(enable ? View.GONE : View.VISIBLE);

        editTextFirstName.setVisibility(enable ? View.VISIBLE : View.GONE);
        editTextLastName.setVisibility(enable ? View.VISIBLE : View.GONE);
        editTextUsername.setVisibility(enable ? View.VISIBLE : View.GONE);
        editTextEmail.setVisibility(enable ? View.VISIBLE : View.GONE);
        buttonChooseFile.setVisibility(enable ? View.VISIBLE : View.GONE);
        buttonChangePassword.setVisibility(enable ? View.VISIBLE : View.GONE);

        profileImage.setClickable(enable);
        buttonSave.setVisibility(enable ? View.VISIBLE : View.GONE);
        buttonEdit.setVisibility(enable ? View.GONE : View.VISIBLE);
        buttonRemovePicture.setVisibility(enable && !isDefaultImage ? View.VISIBLE : View.GONE);

    }

    private void showPasswordFields(boolean show) {

        editTextNewPassword.setVisibility(show ? View.VISIBLE : View.GONE);
        editTextConfirmPassword.setVisibility(show ? View.VISIBLE : View.GONE);

    }

    private void resetPasswordFields() {

        editTextNewPassword.setText("");
        editTextConfirmPassword.setText("");

    }

    private void loadUserProfile() {

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    String firstName = dataSnapshot.child("firstName").getValue(String.class);
                    String lastName = dataSnapshot.child("lastName").getValue(String.class);
                    String username = dataSnapshot.child("username").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String profileImageUrl = dataSnapshot.child("imageUrl").getValue(String.class);

                    if (firstName != null) {
                        textFirstName.setText(firstName);
                        editTextFirstName.setText(firstName);
                    }

                    if (lastName != null) {
                        textLastName.setText(lastName);
                        editTextLastName.setText(lastName);
                    }

                    if (username != null) {
                        textUsername.setText("@" + username);
                        editTextUsername.setText(username);
                    }

                    if (email != null) {
                        textEmail.setText(email);
                        editTextEmail.setText(email);
                    }

                    if (profileImageUrl != null) {
                        Glide.with(Activity_MyAccount.this)
                                .load(profileImageUrl)
                                .into(profileImage);
                        profileImage.clearColorFilter();
                        isDefaultImage = false;
                        if (isEditing) {
                            buttonRemovePicture.setVisibility(View.VISIBLE);
                        }
                    } else {
                        setDefaultProfileImage();
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Activity_MyAccount.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void openFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImage.setImageBitmap(bitmap);
                profileImage.clearColorFilter();
                isDefaultImage = false;
                if (isEditing) {
                    buttonRemovePicture.setVisibility(View.VISIBLE);
                }
            } catch (IOException e) {
                Log.e("Activity_MyAccount", "Failed to load image", e);
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void saveUserProfile() {

        final String firstName = editTextFirstName.getText().toString().trim();
        final String lastName = editTextLastName.getText().toString().trim();
        final String username = editTextUsername.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String newPassword = editTextNewPassword.getText().toString().trim();
        final String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";

        if (!newPassword.isEmpty() && !confirmPassword.isEmpty()) {
            if (!Pattern.matches(pattern, newPassword) && newPassword.length() < 8) {
                Toast.makeText(Activity_MyAccount.this, "Password must contain at least one digit, one lowercase, one uppercase letter, one special character, and be at least 8 characters long", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return;
            }
            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(Activity_MyAccount.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }
            currentUser.updatePassword(newPassword).addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Toast.makeText(Activity_MyAccount.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                }
            });
        }

        Map<String, Object> updates = new HashMap<>();
        if (!firstName.isEmpty()) updates.put("firstName", firstName);
        if (!lastName.isEmpty()) updates.put("lastName", lastName);
        if (!username.isEmpty()) updates.put("username", username);
        if (!email.isEmpty()) updates.put("email", email);

        if (isDefaultImage) {
            updates.put("imageUrl", null);
            updateDatabase(updates, progressDialog);
        } else if (imageUri != null) {
            final StorageReference fileReference = mStorageRef.child(currentUser.getUid() + ".jpg");
            uploadFileWithRetry(fileReference, imageUri, updates, progressDialog, 3); // Attempt 3 retries
        } else {
            updateDatabase(updates, progressDialog);
        }

    }

    private void uploadFileWithRetry(StorageReference fileReference, Uri fileUri, Map<String, Object> updates, ProgressDialog progressDialog, int retryCount) {

        fileReference.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    updates.put("imageUrl", imageUrl);
                    updateDatabase(updates, progressDialog);
                }))
                .addOnFailureListener(e -> {
                    if (retryCount > 0) {
                        Log.e("Activity_MyAccount", "Upload failed, retrying... (" + retryCount + " retries left)", e);
                        uploadFileWithRetry(fileReference, fileUri, updates, progressDialog, retryCount - 1);
                    } else {
                        progressDialog.dismiss();
                        Log.e("Activity_MyAccount", "Failed to upload image after multiple attempts", e);
                        Toast.makeText(Activity_MyAccount.this, "Failed to upload image after multiple attempts", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void updateDatabase(Map<String, Object> updates, ProgressDialog progressDialog) {

        mDatabase.updateChildren(updates).addOnCompleteListener(task -> {
            progressDialog.dismiss();
            if (task.isSuccessful()) {
                Toast.makeText(Activity_MyAccount.this, "Profile updated", Toast.LENGTH_SHORT).show();
                enableEditing(false);
                loadUserProfile();
                resetPasswordFields();
                showPasswordFields(false);
            } else {
                Toast.makeText(Activity_MyAccount.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void removeProfilePicture() {

        imageUri = null;
        isDefaultImage = true;
        profileImage.setImageResource(R.drawable.username_icon);
        profileImage.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(this, R.color.lightTeal), PorterDuff.Mode.SRC_IN));
        buttonRemovePicture.setVisibility(View.GONE);

    }

    private void checkUserRoleAndRedirect() {

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
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
                            goToViewAppointmentActivity();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Activity_MyAccount.this, "Failed to retrieve user role", Toast.LENGTH_SHORT).show();
            }
        });

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

    private void goToViewAppointmentActivity() {

        Intent intent = new Intent(Activity_MyAccount.this, Activity_ViewAppointment.class);
        startActivity(intent);

    }

    private void goToAdminActivity() {

        Intent intent = new Intent(Activity_MyAccount.this, AdminActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

    }

    private void goToDoctorActivity() {

        Intent intent = new Intent(Activity_MyAccount.this, DoctorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

    }

    private void showDiscardChangesDialog() {

        new AlertDialog.Builder(this)
                .setMessage("Do you want to discard changes? Unsaved changes will be lost.")
                .setPositiveButton("Discard", (dialog, which) -> onBackPressed())
                .setNegativeButton("Cancel", null)
                .show();

    }

}