package com.upt.cti.dentalhub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    LinearLayout Info;
    LinearLayout TBD1;
    LinearLayout TBD2;
    LinearLayout TBD3;
    LinearLayout TBD4;

    public FirebaseAuth.AuthStateListener mAuthListener;
    public String mCurrentUserUid;
    public FirebaseAuth mAuth;
    public DatabaseReference mUserRefDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setAuthListener();
        setAuthInstance();

        Info = (LinearLayout)findViewById(R.id.linearlayout1);
        TBD1 = (LinearLayout)findViewById(R.id.linearlayout2);
        TBD2 = (LinearLayout)findViewById(R.id.linearlayout3);
        TBD3 = (LinearLayout)findViewById(R.id.linearlayout4);
        TBD4 = (LinearLayout)findViewById(R.id.linearlayout5);

    }

    public void setAuthInstance() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void setAuthListener() {
        mAuthListener = firebaseAuth -> {

            FirebaseUser user = firebaseAuth.getCurrentUser();

            if (user != null) {
                setUserData(user);
                getIntent();
            } else {
                //user is signed out
                goToLogin();
            }
        };
    }

    public void setUserData(FirebaseUser user) {
        mCurrentUserUid = user.getUid();
    }

    public void setUsersDatabase() {
        mUserRefDatabase = FirebaseDatabase.getInstance("https://dentalhub-1a0c0-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("users");
    }

    private void goToLogin() {

        Intent intent = new Intent(this, Activity_Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //login is a new task
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); //the old task should be cleared so we cannot go back to it
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    private void logout() {
        mAuth.signOut();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //set the message and title from the strings.xml file
        //builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

        //setting message manually and performing action on button click
        builder.setMessage("Do you want to close this app?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> finish())
                .setNegativeButton("No", (dialog, id) -> {
                    //action for 'NO' Button
                    dialog.cancel();
                });

        //creating dialog box
        AlertDialog alert = builder.create();

        alert.show();
    }

}