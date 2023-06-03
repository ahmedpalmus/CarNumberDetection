package com.example.carnumberdetection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

public class PolicePage extends AppCompatActivity {
    String id;
    Button search,reports,profile,contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_page);

        id=getIntent().getStringExtra("id");

        search=findViewById(R.id.search);
        reports=findViewById(R.id.users_rep);
        contact=findViewById(R.id.users_inq);
        profile=findViewById(R.id.client_prof);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PolicePage.this, Search.class);
                startActivity(intent);
                finish();
            }
        });
        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PolicePage.this, ReportList.class);
                intent.putExtra("id", id);
                intent.putExtra("type", "police");
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PolicePage.this, Profile.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PolicePage.this, QuestionList.class);
                intent.putExtra("id", id);
                intent.putExtra("type", "police");
                startActivity(intent);

            }
        });

    }

}