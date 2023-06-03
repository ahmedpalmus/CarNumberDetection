package com.example.carnumberdetection;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class ReportDetails extends AppCompatActivity {
    TextView fullname, date, number, details;
    EditText response;
    ImageView imageView;
    Button det, save, cancel, map;
    String username, type, res;
    UserReport Info;
    String URL = Server.ip + "sendresp.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_details);

        username = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");

        Info = (UserReport) getIntent().getSerializableExtra("item");

        fullname = findViewById(R.id.post_name);
        date = findViewById(R.id.post_add);
        number = findViewById(R.id.car_id);
        imageView = findViewById(R.id.roundedImageView);
        details = findViewById(R.id.post_det);
        det = findViewById(R.id.find_car);
        save = findViewById(R.id.add_response);
        cancel = findViewById(R.id.cancel);
        response = findViewById(R.id.ur_response);
        map = findViewById(R.id.find_map);


        det.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReportDetails.this, CarDetails.class);
                intent.putExtra("number", Info.getPlate_number());
                intent.putExtra("id", username);
                startActivity(intent);
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                res = response.getText().toString().trim();
                if (res.length() > 4) {
                    SendInfo();
                } else {
                    Toast.makeText(getApplicationContext(), "Enter valid Response", Toast.LENGTH_LONG).show();
                }

            }
        });
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenMap(Info.getLat(), Info.getLon());

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        fullname.setText(Info.getFullname());
        date.setText(Info.getScan_date());
        number.setText(Info.getPlate_number());
        details.setText(Info.getDetails());
        response.setText(Info.getResponse());

        if (type.equals("police")) {
            response.setEnabled(true);
        } else {
            response.setEnabled(false);
            save.setVisibility(View.GONE);
            det.setVisibility(View.GONE);

        }
        if(Info.getResponse().trim().length()>2){
            response.setEnabled(false);
            save.setVisibility(View.GONE);
        }
        if (Info.getScan_image().length() > 5) {
            getImage(Info.getScan_image(), imageView);
        }
        if (Info.getLat().length() < 5) {
            map.setVisibility(View.GONE);
        }
    }

    public void OpenMap(String lat, String lon) {
        double latitude = Double.parseDouble(lat); // The latitude value
        double longitude = Double.parseDouble(lon); // The longitude value
        String uri = "http://maps.google.com/maps?daddr=" + latitude + "," + longitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    public void getImage(final String img, final ImageView viewHolder) {
        class packTask extends AsyncTask<Void, Void, Bitmap> {
            @Override
            protected Bitmap doInBackground(Void... voids) {
                Bitmap image1 = null;
                java.net.URL url = null;
                try {
                    url = new URL(Server.ip + img);
                    image1 = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return image1;
            }

            protected void onPostExecute(Bitmap image) {
                viewHolder.setImageBitmap(image);
            }
        }
        packTask t = new packTask();
        t.execute();
    }

    private void SendInfo() {
        class RegAsync extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(ReportDetails.this, getResources().getString(R.string.wait), getResources().getString(R.string.connecting));
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("username", username);
                data.put("report", Info.getProblem_id());
                data.put("response", res);

                String result = con.sendPostRequest(URL, data);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                loadingDialog.dismiss();
                if (result.isEmpty() || result.equals("Error"))
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.check), Toast.LENGTH_LONG).show();
                else if (result.equals("failure")) {
                    Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_LONG).show();
                } else if (result.equalsIgnoreCase("success")) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.success), Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
        RegAsync la = new RegAsync();
        la.execute();
    }

}