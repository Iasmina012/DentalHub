package com.upt.cti.dentalhub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.Objects;

import com.upt.cti.dentalhub.Interfaces.UsernameCheckCallback;
import com.upt.cti.dentalhub.Models.UserModel;

public class Activity_SignUp extends AppCompatActivity {

    EditText userFirstName;
    EditText userLastName;
    EditText userUsername;
    EditText userEmail;
    EditText userPassword;
    EditText userRepassword;
    Button buttonSignUp;
    Button buttonAlreadyMember;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private static final String DEFAULT_PROFILE_IMAGE_URL = "https://cdn-icons-png.flaticon.com/512/1144/1144760.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        userFirstName = findViewById(R.id.editTextFirstName);
        userLastName = findViewById(R.id.editTextLastName);
        userUsername = findViewById(R.id.editTextUsername);
        userEmail = findViewById(R.id.editText_email);
        userPassword = findViewById(R.id.editTextPassword);
        userRepassword = findViewById(R.id.editTextPasswordConfirmation);
        buttonSignUp = findViewById(R.id.btn_SignUp);
        buttonAlreadyMember = findViewById(R.id.btn_Login);

        setAuthInstance();
        setDatabaseInstance();

        buttonAlreadyMember.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), Activity_Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });

        buttonSignUp.setOnClickListener(v -> onRegisterUser());

    }

    private void setAuthInstance() { mAuth = FirebaseAuth.getInstance(); }

    private void setDatabaseInstance() { mDatabase = FirebaseDatabase.getInstance("https://dentalhub-1a0c0-default-rtdb.europe-west1.firebasedatabase.app/").getReference(); }

    public boolean validate() {

        boolean valid = true;

        String firstName = this.userFirstName.getText().toString();
        String lastName = this.userLastName.getText().toString();
        String userName = this.userUsername.getText().toString();
        String email = userEmail.getText().toString();
        String password = this.userPassword.getText().toString();
        String reEnterPassword = userRepassword.getText().toString();

        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
        /*
        (?=.*[0-9]): Cel putin un caracter numeric
        (?=.*[a-z]): Cel putin un caracter litera mica
        (?=.*[A-Z]): Cel putin un caracter litera mare
        (?=.*[@#$%^&+=]): Cel putin un caracter special dintre @#$%^&+=
        (?=\S+$): Nu contine spatii albe
        .{8,}: Lungimea minima a parolei este de 8 caractere
         */

        if(firstName.isEmpty()) {
            this.userFirstName.setError("Enter a valid first name!");
            valid = false;
        } else {
            this.userFirstName.setError(null);
        }

        if(lastName.isEmpty()) {
            this.userLastName.setError("Enter a valid last name!");
            valid = false;
        } else {
            this.userLastName.setError(null);
        }

        if (userName.isEmpty()) {
            this.userUsername.setError("Enter a valid username!");
            valid = false;
        } else {
            this.userUsername.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userEmail.setError("Enter a valid email address!");
            valid = false;
        } else {
            userEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 8 || !password.matches(pattern)) {
            this.userPassword.setError("The password must contain at least one lowercase letter, one uppercase letter, one numeric digit, one special character (@#$%^&+=) and have a minimum length of 8 characters!");
            valid = false;
        } else {
            this.userPassword.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 8 || !reEnterPassword.matches(pattern) || !reEnterPassword.equals(password)) {
            userRepassword.setError("The passwords do not match!");
            valid = false;
        } else {
            userRepassword.setError(null);
        }

        return valid;

    }

    private void onRegisterUser() {

        final String name = userUsername.getText().toString();
        Log.d("Name", " " + name);

        if (!validate()) {
            return;
        }

        retreiveUserNames(name, isTaken -> {
            if (isTaken) {
                userUsername.setError("Enter a unique username!");
            } else {
                signUp(getUserEmail(), getUserPassword());
            }
        });

    }

    public void retreiveUserNames(final String sUserName, final UsernameCheckCallback callback) {

        mDatabase = FirebaseDatabase.getInstance("https://dentalhub-1a0c0-default-rtdb.europe-west1.firebasedatabase.app/").getReference("userNames");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isTaken = false;

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String existingUsername = userSnapshot.getKey();
                    if (sUserName.equals(existingUsername)) {
                        isTaken = true;
                        break;
                    }
                }
                callback.onUsernameCheckComplete(isTaken);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Connection Error! Please try again later!", Toast.LENGTH_SHORT).show();
                callback.onUsernameCheckComplete(true); // Consider it as taken in case of error
            }
        });

    }

    private String getUserFirstName() { return userFirstName.getText().toString().trim(); }

    private String getUserLastName() { return userLastName.getText().toString().trim(); }

    private String getUserUsername() { return userUsername.getText().toString().trim(); }

    private String getUserEmail() { return userEmail.getText().toString().trim(); }

    private String getUserPassword() { return userPassword.getText().toString().trim(); }

    private void signUp(String email, String password) {

        final ProgressDialog progressDialog = new ProgressDialog(Activity_SignUp.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            progressDialog.dismiss();

            if(task.isSuccessful()) {
                onAuthSuccess(Objects.requireNonNull(task.getResult().getUser()));
            }
            else {
                Toast.makeText(getApplicationContext(), " " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void onAuthSuccess(FirebaseUser user) {

        createNewUser(user.getUid());
        createUserNames();
        goToMainActivity();

    }

    private void goToMainActivity() {

        Intent intent = new Intent(Activity_SignUp.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    private void createNewUser(String userId){

        UserModel user = buildNewUser();
        FirebaseDatabase.getInstance("https://dentalhub-1a0c0-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users").child(userId).setValue(user);

    }

    public void createUserNames() {

        FirebaseDatabase.getInstance("https://dentalhub-1a0c0-default-rtdb.europe-west1.firebasedatabase.app/").getReference("userNames").child(getUserUsername()).setValue(true);

    }

    private UserModel buildNewUser() {

        return new UserModel(getUserFirstName(), getUserLastName(), getUserUsername(), getUserEmail(), new Date().getTime(), DEFAULT_PROFILE_IMAGE_URL);

    }

}