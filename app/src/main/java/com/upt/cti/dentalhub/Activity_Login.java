package com.upt.cti.dentalhub;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;


public class Activity_Login extends AppCompatActivity {

    EditText userEmail;
    EditText userPassword;
    Button signIn;
    Button forgotPassword;
    Button newUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmail = findViewById(R.id.editTextUserEmail);
        userPassword = findViewById(R.id.editTextPassword);
        signIn = findViewById(R.id.buttonSignIn);
        forgotPassword = findViewById(R.id.buttonForgotPassword);
        newUser = findViewById(R.id.buttonNewUser);
        setAuthInstance();

        signIn.setOnClickListener(v -> onLogInUser());

        newUser.setOnClickListener(v -> goToRegisterActivity());

        forgotPassword.setOnClickListener(v -> forgotYourPassword());

    }

    private String getUserEmail() {
        return userEmail.getText().toString().trim();
    }

    private String getUserPassword() {
        return userPassword.getText().toString().trim();
    }

    public boolean validate() {

        boolean valid = true;
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";

        /*
        (?=.*[0-9]): Cel putin un caracter numeric
        (?=.*[a-z]): Cel putin un caracter litera mica
        (?=.*[A-Z]): Cel putin un caracter litera mare
        (?=.*[@#$%^&+=]): Cel putin un caracter special dintre @#$%^&+=
        (?=\S+$): Nu contine spatii albe
        .{8,}: Lungimea minima a parolei este de 8 caractere
         */

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userEmail.setError("Enter a valid email address!");
            valid = false;
        } else {
            userPassword.setError(null);
        }

        if (password.isEmpty() || (password.length() < 8 || !password.matches(pattern))) {
            userPassword.setError("The password must contain at least one lowercase letter, one uppercase letter, one numeric digit, one special character (@#$%^&+=) and have a minimum length of 8 characters!");
            valid = false;
        } else {
            userPassword.setError(null);
        }

        return valid;

    }

    private void setAuthInstance() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void onLogInUser() {

        if (!validate()) {
            //nothing happens
        } else {
            logIn(getUserEmail(), getUserPassword());
        }

    }

    private void logIn(String email, String password) {

        final ProgressDialog progressDialog = new ProgressDialog(Activity_Login.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Logging In...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {

            progressDialog.dismiss();

            if (task.isSuccessful()) {
                goToMainActivity();
            } else {
                Toast.makeText(getApplicationContext(), " " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                userPassword.setText("");
                userPassword.requestFocus();
            }

        });

    }

    private void goToMainActivity() {

        Intent intent = new Intent(Activity_Login.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

    }

    private void goToRegisterActivity() {

        Intent i = new Intent(getApplicationContext(), Activity_SignUp.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

    }

    public void forgotYourPassword() {

        LayoutInflater li = LayoutInflater.from(Activity_Login.this);
        View promptsView = li.inflate(R.layout.prompt_email, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Activity_Login.this);
        alertDialogBuilder.setView(promptsView);

        final EditText userMail = promptsView.findViewById(R.id.editTextDialogUserInput);

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", null)
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        //initially disable the OK button
        final Button okButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        okButton.setEnabled(false);

        //add text watcher to the email field to enable/disable the OK button
        userMail.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {

                String email = userMail.getText().toString().trim();
                if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    userMail.setError("Enter a valid email address!");
                    okButton.setEnabled(false);
                } else {
                    userMail.setError(null);
                    okButton.setEnabled(true);
                }

            }
        });

        //set the positive button click listener
        okButton.setOnClickListener(v -> {
            String email = userMail.getText().toString().trim();
            if (!email.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                checkEmailExistsAndSendReset(email, alertDialog);
            } else {
                userMail.setError("Enter a valid email address!");
            }
        });

    }

    private void checkEmailExistsAndSendReset(String email, AlertDialog alertDialog) {

        DatabaseReference usersRef = FirebaseDatabase.getInstance("https://dentalhub-1a0c0-default-rtdb.europe-west1.firebasedatabase.app").getReference("users");
        usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    //email exists, send the reset email
                    mAuth.sendPasswordResetEmail(email)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(Activity_Login.this, "Reset password email sent successfully", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            })
                            .addOnFailureListener(e -> Toast.makeText(Activity_Login.this, "Failed to send reset password email: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                } else {
                    //email does not exist
                    Toast.makeText(Activity_Login.this, "No account found with this email address", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Activity_Login.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}