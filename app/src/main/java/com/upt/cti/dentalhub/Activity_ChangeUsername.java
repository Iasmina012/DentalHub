package com.upt.cti.dentalhub;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Activity_ChangeUsername extends BaseActivity {

    private EditText usernameEditText;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);

        usernameEditText = findViewById(R.id.usernameEditText);
        Button saveUsernameButton = findViewById(R.id.saveUsernameButton);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance("https://dentalhub-1a0c0-default-rtdb.europe-west1.firebasedatabase.app").getReference("users").child(currentUser.getUid());

        saveUsernameButton.setOnClickListener(v -> updateUsername());

    }

    private void updateUsername() {

        String newUsername = usernameEditText.getText().toString().trim();
        if (!newUsername.isEmpty()) {
            databaseReference.child("displayName").setValue(newUsername)
                    .addOnSuccessListener(aVoid -> Toast.makeText(Activity_ChangeUsername.this, "Username updated", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(Activity_ChangeUsername.this, "Update failed", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show();
        }

    }

}