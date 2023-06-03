package com.example.carnumberdetection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements GoogleMap.OnMapLongClickListener, OnMapReadyCallback {
    GoogleMap map;
    double lat, lon;
    String place;
    GoogleMap.OnMapLongClickListener onMapLongClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        lat = 18.083908832720883;
        lon = 42.707147329617236;
        place = "KKU University";
        onMapLongClickListener = this;
        if (getIntent().hasExtra("lat")) {
            lat = Double.valueOf(getIntent().getStringExtra("lat"));
            lon = Double.valueOf(getIntent().getStringExtra("lon"));
            place = "location";
            onMapLongClickListener = null;
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapLongClickListener(onMapLongClickListener);
        LatLng Jeddah = new LatLng(lat, lon);
        map.getUiSettings().setMapToolbarEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.animateCamera(CameraUpdateFactory.newLatLng(Jeddah));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(Jeddah, 17));
        map.addMarker(new MarkerOptions().position(Jeddah).title(place));
    }


    @Override
    public void onMapLongClick(LatLng latLng) {
        Intent intent = new Intent();
        intent.putExtra("lat", "" + latLng.latitude);
        intent.putExtra("lon", "" + latLng.longitude);
        setResult(Activity.RESULT_OK, intent); //The data you want to send back
        finish();
    }
}
