package com.example.carnumberdetection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

public class UserPage extends AppCompatActivity {
    String id;
    Button scan,reports,profile,contact;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        id=getIntent().getStringExtra("id");

        scan=findViewById(R.id.scan);
        reports=findViewById(R.id.reports);
        contact=findViewById(R.id.contact);
        profile=findViewById(R.id.client_prof);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(UserPage.this, PlateDetectionActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserPage.this, ReportList.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        System.out.println("good2");

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            System.out.println("good3");

            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
}