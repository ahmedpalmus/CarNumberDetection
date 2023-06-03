package com.example.carnumberdetection;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Search extends AppCompatActivity {
    TextView fullname, color,brand,id,phone, details;
    Button cancel,search;
    String car_number;
    String URL = Server.ip + "getcar.php";
EditText number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        fullname = findViewById(R.id.name);
        color = findViewById(R.id.color);
        number = findViewById(R.id.car_id);
        brand = findViewById(R.id.brand);
        details = findViewById(R.id.ur_response);
        id = findViewById(R.id.id);
        phone = findViewById(R.id.phone);
        cancel = findViewById(R.id.cancel);
        search = findViewById(R.id.find_car);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                car_number=number.getText().toString().trim();
                if (car_number.length()>4){
                    getInfos();
                }else{
                    Toast.makeText(getApplicationContext(), "Enter valid car number", Toast.LENGTH_LONG).show();

                }

            }
        });

    }
    private void getInfos() {
        class Async extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(Search.this, "please waite...", "Connecting....");
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