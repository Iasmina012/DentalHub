package com.upt.cti.dentalhub;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PromptMenuActivity extends AppCompatActivity {

    public FirebaseAuth.AuthStateListener mAuthListener;
    public FirebaseAuth mAuth;
    public String mCurrentUserUid;
    public DatabaseReference mUserRefDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAuthInstance();
        setAuthListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_back) {
            new AlertDialog.Builder(this)
                    .setTitle("Confirmation")
                    .setMessage("Are you sure you want to exit? This will cancel the current action.")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        onBackPressed();
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        } else if (id == R.id.action_home) {
            new AlertDialog.Builder(this)
                    .setTitle("Confirmation")
                    .setMessage("Are you sure you want to exit? This will cancel the current action.")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        checkUserRoleAndRedirect();
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        } else if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent logoutIntent = new Intent(this, Activity_Login.class);
            logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(logoutIntent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            return true;
        } else if (id == R.id.action_my_account) {
            Intent changeUsernameIntent = new Intent(this, Activity_MyAccount.class);
            startActivity(changeUsernameIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setAuthInstance() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void setAuthListener() {
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                setUserData(user);
            } else {
                goToLogin();
            }
        };
    }

    public void setUserData(FirebaseUser user) {
        mCurrentUserUid = user.getUid();
        setUsersDatabase();
    }

    public void setUsersDatabase() {
        mUserRefDatabase = FirebaseDatabase.getInstance("https://dentalhub-1a0c0-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("users");
    }

    private void goToLogin() {
        Intent intent = new Intent(this, Activity_Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void checkUserRoleAndRedirect() {
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
                    Toast.makeText(PromptMenuActivity.this, "Failed to retrieve user role", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            goToLogin();
        }
    }

    private boolean isAdmin(String email) {
        return "admin@dentalhub.com".equals(email); // Update with the actual admin email if different
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
        Intent intent = new Intent(PromptMenuActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void goToAdminActivity() {
        Intent intent = new Intent(PromptMenuActivity.this, AdminActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void goToDoctorActivity() {
        Intent intent = new Intent(PromptMenuActivity.this, DoctorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth != null && mAuthListener != null) {
            mAuth.addAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuth != null && mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
