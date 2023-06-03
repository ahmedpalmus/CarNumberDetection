package com.example.carnumberdetection;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class NewReport extends AppCompatActivity {
    TextView plate;
    EditText details;
    String number, username,detail,Image = "none",lat="",lon="";
    ImageView image;
    Button save, cancel, img_btn, map;
    String URL = Server.ip + "add_report.php";

    // Create lanucher variable inside onAttach or onCreate or global
    ActivityResultLauncher<Intent> launchMap = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    lat = data.getStringExtra("lat");
                    lon =data.getStringExtra("lon");

                }
            }
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_report);
        plate = findViewById(R.id.plate);
        details = findViewById(R.id.help_details);
        image = findViewById(R.id.add_pimage);
        map = findViewById(R.id.help_location);
        img_btn = findViewById(R.id.help_image);
        cancel = findViewById(R.id.help_cancel);
        save = findViewById(R.id.help_save);
        username = getIntent().getStringExtra("username");
        number = getIntent().getStringExtra("number");
        plate.setText(number);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Save();
            }
        });

        img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser(400);
            }

        });
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewReport.this, MapActivity.class);
                launchMap.launch(intent);
            }
        });

    }

    private void showFileChooser(int code) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), code);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 400 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedimg = data.getData();
            try {
                image.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg));
                Bitmap univLogo = ((BitmapDrawable) image.getDrawable()).getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                univLogo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                Image = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void Save() {

        detail = details.getText().toString().trim();
        if (detail.length() < 2) {
            Toast.makeText(getApplicationContext(), "Enter the details", Toast.LENGTH_LONG).show();

        }else {
            SendInfo();
        }
    }

    private void SendInfo() {
        class RegAsync extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(NewReport.this, getResources().getString(R.string.wait), getResources().getString(R.string.connecting));
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("username", username);
                data.put("number", number);
                data.put("details", detail);
                data.put("image", Image);
                data.put("lat", lat);
                data.put("lon", lon);

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