package com.upt.cti.dentalhub;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class MyFirebaseApp extends Application {

    @Override
    public void onCreate() {

        super.onCreate();
        FirebaseDatabase.getInstance("https://dentalhub-1a0c0-default-rtdb.europe-west1.firebasedatabase.app/").setPersistenceEnabled(true);

    }

}