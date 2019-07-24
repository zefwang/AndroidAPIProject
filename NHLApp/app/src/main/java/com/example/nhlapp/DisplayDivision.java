package com.example.nhlapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class DisplayDivision extends AppCompatActivity {
    TextView divName;
    JSONObject divTeams;
    TableLayout divTable;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_division);

        divName = (TextView) findViewById(R.id.division_name);
        divName.setText(getIntent().getStringExtra("DIVISION_NAME"));

        try {
            divTeams = new JSONObject(getIntent().getStringExtra("DIVISION_TEAMS"));
        } catch (JSONException e){
            e.printStackTrace();
        }

        divTable = (TableLayout) findViewById(R.id.divisionTable);


    }
}
