package com.upt.cti.dentalhub;

import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class Activity_Contact extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int LOCATION_REQUEST_CODE = 101;
    private boolean locationNotFoundShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setQueryHint("Search for an office here ...");

        findViewById(R.id.floatingActionButton).setOnClickListener(view -> {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.popup_message);
            Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.show();

            FloatingActionButton fab = dialog.findViewById(R.id.floatingActionButton);
            fab.setOnClickListener(v -> {
                EditText nameField = dialog.findViewById(R.id.editTextName);
                EditText emailField = dialog.findViewById(R.id.editTextEmail);
                EditText messageField = dialog.findViewById(R.id.editTextMessage);

                String name = nameField.getText().toString();
                String email = emailField.getText().toString();
                String message = messageField.getText().toString();

                if (name.isEmpty()) {
                    nameField.setError("Enter your name!");
                } else {
                    nameField.setError(null);
                }

                if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailField.setError("Enter a valid email address!");
                } else {
                    emailField.setError(null);
                }

                if (message.isEmpty()) {
                    messageField.setError("Enter your message!");
                } else {
                    messageField.setError(null);
                }

                if (!name.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && !message.isEmpty()) {
                    sendEmail(name, email, message);
                    dialog.dismiss();
                }

            });

            ImageButton closeButton = dialog.findViewById(R.id.buttonClose);
            closeButton.setOnClickListener(v -> dialog.dismiss());

        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                searchLocation(query);
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {return false;}
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;
        //sets the initial location to headquarters
        setInitialLocation();
        enableMyLocation();

    }

    private void setInitialLocation() {

        String location = "1425 Broadway Suite 22, New York, NY 10018";
        LatLng latLng = getLocationFromAddress(location);
        if (latLng != null) {
            mMap.addMarker(new MarkerOptions().position(latLng).title("Headquarters"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }

    }

    private void searchLocation(String location) {

        locationNotFoundShown = false;
        LatLng latLng = getLocationFromAddress(location);
        if (latLng != null) {
            mMap.addMarker(new MarkerOptions().position(latLng).title("Search Result"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            locationNotFoundShown = false;
        } else {
            showLocationNotFoundMessage();
        }

    }

    private LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;
        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null || address.isEmpty()) {
                return null;
            }
            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (IOException ex) {
            Log.e("LocationError", "Failed to get location from address!", ex);
        }
        return p1;

    }

    private void showLocationNotFoundMessage() {

        if (!locationNotFoundShown) {
            //cand apas pe tastatura apare o data, cand apas pe enter apare de 2 ori
            //!de verificat!
            runOnUiThread(() -> Toast.makeText(this, "Location not found!", Toast.LENGTH_SHORT).show());
            locationNotFoundShown = true;
        }

    }

    private void enableMyLocation() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_CODE);
        } else {
            mMap.setMyLocationEnabled(true);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
            } else {
                Toast.makeText(this, "Location permission denied!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void sendEmail(String name, String email, String message) {

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"iasmina.putina012@yahoo.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "New message from " + name + " (" + email + ")");
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent, "Send email via..."));

    }

}