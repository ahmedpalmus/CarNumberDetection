package com.example.carnumberdetection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserPage extends AppCompatActivity {
    String id;
    Button scan,reports,profile,contact;
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
}