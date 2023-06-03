package com.example.carnumberdetection;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ReportList extends AppCompatActivity {
    String id,type;


    private final String URL = Server.ip + "getreports.php";
    ArrayList<UserReport> records;
    ListView simpleList;

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);

        id=getIntent().getStringExtra("id");
        type=getIntent().getStringExtra("type");


        simpleList = findViewById(R.id.report_list);
        records = new ArrayList<>();

        getInfos();
    }

    protected void onResume() {
        super.onResume();
        getInfos();
    }
    private void getInfos() {
        class Async extends AsyncTask<String, Void, String> {
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(ReportList.this, "please waite...", "Connecting....");
            }

            @Override
            protected String doInBackground(String... params) {
                Connection con = new Connection();
                HashMap<String, String> data = new HashMap<>();
                data.put("username", id);
                data.put("type", type);

                String result = con.sendPostRequest(URL, data);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                loadingDialog.dismiss();
                records.clear();
                String res1[] = new String[0];
                adapter= new ArrayAdapter<>(getApplicationContext(), R.layout.menu_view, R.id.menu_n, res1);
                simpleList.setAdapter(adapter);
                adapter.clear();
                adapter.notifyDataSetChanged();
                if (result.isEmpty() || result.equals("Error"))
                    Toast.makeText(getApplicationContext(), "Check connection", Toast.LENGTH_LONG).show();
                else if (result.equals("failure")) {
                    Toast.makeText(getApplicationContext(), "try again", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        records = new ArrayList<>();
                        JSONArray allReq = new JSONArray(result);
                        for (int i = 0; i < allReq.length(); i++) {
                            JSONObject row = allReq.getJSONObject(i);

                            UserReport temp=new UserReport();
                            temp.setProblem_id(row.getString("problem_id"));
                            temp.setFullname(row.getString("fullname"));
                            temp.setUsername(row.getString("username"));
                            temp.setPlate_number(row.getString("plate_number"));
                            temp.setLat(row.getString("lat"));
                            temp.setLon(row.getString("lon"));
                            temp.setState(row.getString("state"));
                            temp.setScan_date(row.getString("scan_date"));
                            temp.setScan_image(row.getString("scan_image"));
                            temp.setDetails(row.getString("details"));
                            temp.setResponse(row.getString("response"));

                            records.add(temp);

                        }

                        String res[] = new String[records.size()];
                        for (int j = 0; j < records.size(); j++) {
                            res[j] =records.get(j).getFullname()+"\n"+records.get(j).getScan_date();
                        }
                        adapter= new ArrayAdapter<>(getApplicationContext(), R.layout.menu_view, R.id.menu_n,res);
                        simpleList.setAdapter(adapter);
                        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long idd) {

                                Intent intent = new Intent(ReportList.this, ReportDetails.class);
                                intent.putExtra("item", records.get(position));
                                intent.putExtra("type", type);
                                intent.putExtra("id", id);
                                startActivity(intent);
                            }
                        });

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