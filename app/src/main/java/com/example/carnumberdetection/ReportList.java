package com.example.carnumberdetection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ReportList extends AppCompatActivity {
    String id;
    Button new_report;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);

        id=getIntent().getStringExtra("id");

        new_report=findViewById(R.id.new_report);


        new_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReportList.this, NewReport.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });


    }
}