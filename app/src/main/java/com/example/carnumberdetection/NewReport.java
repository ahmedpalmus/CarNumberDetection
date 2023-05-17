package com.example.carnumberdetection;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class NewReport extends AppCompatActivity {
TextView plate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_report);
        plate=findViewById(R.id.plate);
        String number=getIntent().getStringExtra("number");
        plate.setText(number);
    }
}