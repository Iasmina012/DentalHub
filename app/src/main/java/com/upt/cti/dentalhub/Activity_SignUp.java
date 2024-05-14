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

public class Activity_SignUp extends AppCompatActivity {

    EditText username;
    EditText userEmail;
    EditText password;
    EditText repassword;
    Button btn_Sign_Up;
    Button btn_Already_Member;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private boolean isTaken = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        username = findViewById(R.id.editTextUsername);
        userEmail = findViewById(R.id.editText_email);
        password = findViewById(R.id.editTextPassword);
        repassword = findViewById(R.id.editTextPasswordConfirmation);
        btn_Sign_Up = findViewById(R.id.btn_SignUp);
        btn_Already_Member = findViewById(R.id.btn_Login);

        setAuthInstance();
        setDatabaseInstance();

        btn_Already_Member.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), Activity_Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });

        btn_Sign_Up.setOnClickListener(v -> onRegisterUser());

    }

    private void setAuthInstance() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void setDatabaseInstance() {mDatabase = FirebaseDatabase.getInstance("https://dentalhub-1a0c0-default-rtdb.europe-west1.firebasedatabase.app/").getReference();}

    public boolean validate() {

        boolean valid = true;

        String name = username.getText().toString();
        String email = userEmail.getText().toString();
        String password = this.password.getText().toString();
        String reEnterPassword = repassword.getText().toString();

        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";

        /*
        (?=.*[0-9]): Cel putin un caracter numeric
        (?=.*[a-z]): Cel putin un caracter litera mica
        (?=.*[A-Z]): Cel putin un caracter litera mare
        (?=.*[@#$%^&+=]): Cel putin un caracter special dintre @#$%^&+=
        (?=\S+$): Nu contine spatii albe
        .{8,}: Lungimea minima a parolei este de 8 caractere
         */

        if (name.isEmpty()) {
            username.setError("Enter a valid username!");
            valid = false;
        } else {
            username.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userEmail.setError("Enter a valid email address!");
            valid = false;
        } else {
            userEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 8 || !password.matches(pattern)) {
            this.password.setError("The password must contain at least one lowercase letter, one uppercase letter, one numeric digit, one special character (@#$%^&+=) and have a minimum length of 8 characters!");
            valid = false;
        } else {
            this.password.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 8 || !reEnterPassword.matches(pattern) || !reEnterPassword.equals(password)) {
            repassword.setError("The passwords do not match!");
            valid = false;
        } else {
            repassword.setError(null);
        }
        return valid;
    }

    private void onRegisterUser() {

        String name = username.getText().toString();
        Log.d("Name", " " + name);
        boolean exist = retreiveUserNames(name);

        if (!validate()) {
            //daca validarea nu trece, nu facem nimic si lasam erorile sa fie afisate
        } else if (exist) {
            username.setError("Enter a unique username!");
        } else {
            signUp(getUserEmail(), getUserPassword());
        }

    }

    public boolean retreiveUserNames(final String sUserName) {

        mDatabase = FirebaseDatabase.getInstance("https://dentalhub-1a0c0-default-rtdb.europe-west1.firebasedatabase.app/").getReference("userNames");
        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String existingUsername = userSnapshot.getKey();

                    Log.d("Ex"," "+sUserName);
                    Log.d("Shot ", " " + existingUsername);

                    if(sUserName.equals(existingUsername)) {
                        isTaken = true;
                        Log.d("BooleanShot ", "" + isTaken);
                        break;
                    }
                    else {
                        isTaken = false;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {Toast.makeText(getApplicationContext(), "Connection Error! Please try again later!", Toast.LENGTH_SHORT).show();}
        });
        return isTaken;

    }

    private String getUserDisplayName() {
        return username.getText().toString().trim();
    }

    private String getUserEmail() {
        return userEmail.getText().toString().trim();
    }

    private String getUserPassword() {
        return password.getText().toString().trim();
    }

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

        Intent intent = new Intent(Activity_SignUp.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    private void createNewUser(String userId){

        UserModel user = buildNewUser();
        FirebaseDatabase.getInstance("https://dentalhub-1a0c0-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users").child(userId).setValue(user);

    }

    public void createUserNames() {

        //mDatabase.child("userNames").child(getUserDisplayName()).setValue(true);
        FirebaseDatabase.getInstance("https://dentalhub-1a0c0-default-rtdb.europe-west1.firebasedatabase.app/").getReference("userNames").child(getUserDisplayName()).setValue(true);

    }
    private UserModel buildNewUser() {return new UserModel(getUserDisplayName(), getUserEmail(), new Date().getTime());}

}