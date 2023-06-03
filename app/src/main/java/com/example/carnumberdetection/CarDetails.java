package com.example.carnumberdetection;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class CarDetails extends AppCompatActivity {
    TextView fullname, color,brand,id,phone, number, details;
    Button cancel;
    String car_number,username;
    String URL = Server.ip + "getcar.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);

        username = getIntent().getStringExtra("id");
        car_number = getIntent().getStringExtra("number");



        fullname = findViewById(R.id.name);
        color = findViewById(R.id.color);
        number = findViewById(R.id.car_id);
        brand = findViewById(R.id.brand);
        details = findViewById(R.id.ur_response);
        id = findViewById(R.id.id);
        phone = findViewById(R.id.phone);
        cancel = findViewById(R.id.cancel);
        number.setText(car_number);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

getInfos();

    }
    private void getInfos() {
        class Async extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(CarDetails.this, "please waite...", "Connecting....");
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("car_id", car_number);

                String result = con.sendPostRequest(URL, data);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                loadingDialog.dismiss();
                if (result.isEmpty() || result.equals("Error"))
                    Toast.makeText(getApplicationContext(), "Check connection", Toast.LENGTH_LONG).show();
                else if (result.equals("failure")) {
                    Toast.makeText(getApplicationContext(), "No Car information", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONArray allReq = new JSONArray(result);
                        JSONObject row = allReq.getJSONObject(0);
                        color.setText(row.getString("color"));
                        brand.setText(row.getString("brand"));
                        id.setText(row.getString("owner_id"));
                        fullname.setText(row.getString("owner_name"));
                        phone.setText(row.getString("phone"));
                        details.setText(row.getString("details"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        Async la = new Async();
        la.execute();
    }

}