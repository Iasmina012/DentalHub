package com.upt.cti.dentalhub;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import java.util.ArrayList;
import java.util.List;

public class Activity_ViewAppointment extends AppCompatActivity {

    private static final String TAG = "ViewAppointments";

    private RecyclerView recyclerViewAppointments;
    private TextView textViewNoAppointments;
    private DatabaseReference db;
    private AppointmentAdapter adapter;
    private List<Appointment> appointmentList;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointment);

        recyclerViewAppointments = findViewById(R.id.recyclerViewAppointments);
        textViewNoAppointments = findViewById(R.id.textViewNoAppointments);

        db = FirebaseDatabase.getInstance("https://dentalhub-1a0c0-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("appointments");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Log.e(TAG, "User not authenticated!");
            finish();
            return;
        }

        appointmentList = new ArrayList<>();
        adapter = new AppointmentAdapter(appointmentList, this);
        recyclerViewAppointments.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAppointments.setAdapter(adapter);

        loadAppointments();

    }

    private void loadAppointments() {

        db.orderByChild("userId").equalTo(currentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        appointmentList.clear();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Appointment currentAppointment = snapshot.getValue(Appointment.class);
                                if (currentAppointment != null) {
                                    appointmentList.add(currentAppointment);
                                    Log.d(TAG, "Appointment added: " + currentAppointment);
                                }
                            }
                        } else {
                            Log.d(TAG, "No appointments found for user: " + currentUser.getUid());
                        }

                        if (appointmentList.isEmpty()) {
                            textViewNoAppointments.setVisibility(View.VISIBLE);
                            recyclerViewAppointments.setVisibility(View.GONE);
                            Log.d(TAG, "No appointments available");
                        } else {
                            textViewNoAppointments.setVisibility(View.GONE);
                            recyclerViewAppointments.setVisibility(View.VISIBLE);
                            adapter.notifyDataSetChanged();
                            Log.d(TAG, "Appointments loaded, count: " + appointmentList.size());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(TAG, "Database error: " + databaseError.getMessage());
                    }
                });

    }
}
