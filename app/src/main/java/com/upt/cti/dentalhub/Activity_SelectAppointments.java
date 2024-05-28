package com.upt.cti.dentalhub;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Activity_SelectAppointments extends MainMenuActivity {

    private static final String TAG = "SelectAppointments";
    private Button buttonBookAppointment, buttonViewAppointment;
    private TextView textViewWelcome;
    private ImageView profileImage;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference db;
    private StorageReference mStorageRef;
    private boolean isDefaultImage = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_appointments);

        buttonBookAppointment = findViewById(R.id.buttonBookAppointment);
        buttonViewAppointment = findViewById(R.id.buttonViewAppointment);
        textViewWelcome = findViewById(R.id.textViewWelcome);
        profileImage = findViewById(R.id.imageViewUser);

        db = FirebaseDatabase.getInstance("https://dentalhub-1a0c0-default-rtdb.europe-west1.firebasedatabase.app").getReference("users");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance("gs://dentalhub-1a0c0.appspot.com").getReference("profile_images");

        setDefaultProfileImage();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            db.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        String firstName = dataSnapshot.child("firstName").getValue(String.class);
                        String lastName = dataSnapshot.child("lastName").getValue(String.class);

                        if (firstName != null && lastName != null) {
                            textViewWelcome.setText("Welcome, " + firstName + " " + lastName + "!");
                        } else {
                            Log.d(TAG, "First name or last name not found.");
                            textViewWelcome.setText("Welcome!");
                        }

                        loadProfileImage(userId);
                    } else {
                        Log.d(TAG, "User data not found.");
                        textViewWelcome.setText("Welcome!");
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    textViewWelcome.setText("Welcome!");
                    Log.e(TAG, "Database error: " + databaseError.getMessage());

                }
            });
        } else {
            textViewWelcome.setText("Welcome!");
        }

        buttonBookAppointment.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_SelectAppointments.this, Activity_SelectLocation.class);
            startActivity(intent);
        });

        buttonViewAppointment.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_SelectAppointments.this, Activity_ViewAppointment.class);
            startActivity(intent);
        });

    }

    private void setDefaultProfileImage() {

        profileImage.setImageResource(R.drawable.username_icon);
        profileImage.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(this, R.color.lightTeal), PorterDuff.Mode.SRC_IN));
        isDefaultImage = true;

    }

    private void loadProfileImage(String userId) {

        db.child(userId).child("imageUrl").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    String imageUrl = dataSnapshot.getValue(String.class);
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(Activity_SelectAppointments.this)
                                .load(imageUrl)
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                        setDefaultProfileImage();
                                        Log.e(TAG, "Failed to load profile image", e);
                                        return false;

                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                        runOnUiThread(() -> {
                                            profileImage.clearColorFilter(); // Remove the filter when the image is loaded
                                            Log.d(TAG, "Profile image loaded, color filter cleared");
                                            isDefaultImage = false;
                                        });
                                        return false;

                                    }
                                })
                                .into(profileImage);
                    } else {
                        setDefaultProfileImage();
                    }
                } else {
                    setDefaultProfileImage();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                setDefaultProfileImage();
                Log.e(TAG, "Database error: " + databaseError.getMessage());

            }
        });

    }

}