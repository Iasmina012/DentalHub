package com.upt.cti.dentalhub;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Activity_ChangeEmail extends StaffMenuActivity {

    private EditText emailEditText;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        emailEditText = findViewById(R.id.editTextNewEmail);
        Button saveEmailButton = findViewById(R.id.saveEmailButton);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance("https://dentalhub-1a0c0-default-rtdb.europe-west1.firebasedatabase.app").getReference("users").child(currentUser.getUid());

        saveEmailButton.setOnClickListener(v -> updateEmail());

    }

    private void updateEmail() {

        String newEmail = emailEditText.getText().toString().trim();
        if (!newEmail.isEmpty()) {
            databaseReference.child("email").setValue(newEmail)
                    .addOnSuccessListener(aVoid -> Toast.makeText(Activity_ChangeEmail.this, "Email updated", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(Activity_ChangeEmail.this, "Update failed", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show();
        }

    }

}