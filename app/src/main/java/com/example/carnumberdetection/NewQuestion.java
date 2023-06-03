package com.example.carnumberdetection;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class NewQuestion extends AppCompatActivity {

    String username,Detail;
    EditText detail;
    String URL = Server.ip + "sendquestion.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_question);
        username = getIntent().getStringExtra("id");

        detail=findViewById(R.id.sub_details);
    }

    public void NewCompClick(View view) {
        if (view.getId() == R.id.sub_save) {
            Detail=detail.getText().toString().trim();
            if(Detail.length()<5){
                Toast.makeText(NewQuestion.this,"Enter valid question",Toast.LENGTH_LONG).show();
            }else{
                sendComp();
            }

        }
    }
    private void sendComp() {
        class RegAsync extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(NewQuestion.this, getResources().getString(R.string.wait), getResources().getString(R.string.connecting));
            }
            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("id", username);
                data.put("detail", Detail);
                String result = con.sendPostRequest(URL, data);
                return result;
            }
            @Override
            protected void onPostExecute(String result) {
                loadingDialog.dismiss();
                if (result.isEmpty() || result.equals("Error"))
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.check), Toast.LENGTH_LONG).show();
                else if (result.equals("failure")) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.noResult), Toast.LENGTH_LONG).show();
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
