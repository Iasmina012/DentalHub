package com.upt.cti.dentalhub;

import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.EditText;
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

public class Activity_Contact extends AppCompatActivity implements OnMapReadyCallback {

    private SearchView searchView;
    private GoogleMap mMap;
    private FloatingActionButton fab;

    private static final int LOCATION_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        searchView = findViewById(R.id.searchView);
        searchView.setQueryHint("Search for an office here ...");

        findViewById(R.id.floatingActionButton).setOnClickListener(view -> {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.popup_message);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.show();

            FloatingActionButton fab = dialog.findViewById(R.id.floatingActionButton);
            fab.setOnClickListener(v -> {
                EditText nameField = dialog.findViewById(R.id.editTextName);
                EditText emailField = dialog.findViewById(R.id.editTextEmail);
                EditText messageField = dialog.findViewById(R.id.editTextMessage);

                String name = nameField.getText().toString();
                String email = emailField.getText().toString();
                String message = messageField.getText().toString();
                sendEmail(name, email, message);
                dialog.dismiss();
            });
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchLocation(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Set the initial location to "1425 Broadway Suite 22, New York, NY 10018"
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
        LatLng latLng = getLocationFromAddress(location);
        if (latLng != null) {
            mMap.addMarker(new MarkerOptions().position(latLng).title("Search Result"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        } else {
            Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
        }
    }

    private LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;
        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return p1;
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
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendEmail(String name, String email, String message) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"iasmina.putina012@yahoo.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "New message from " + name);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent, "Send email via..."));
    }
}