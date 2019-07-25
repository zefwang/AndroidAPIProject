package com.example.nhlapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DisplayDivision extends AppCompatActivity {
    TextView divName;
    JSONArray divTeams;
    TableLayout divTable;
    List<JSONObject> teamList;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_division);

        divName = (TextView) findViewById(R.id.division_name);
        divName.setText(getIntent().getStringExtra("DIVISION_NAME"));

        teamList = new ArrayList<>();

        try {
            divTeams = new JSONArray(getIntent().getStringExtra("DIVISION_TEAMS"));

            for(int i = 0; i < divTeams.length(); i++){
                teamList.add(divTeams.getJSONObject(i));
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

        divTable = (TableLayout) findViewById(R.id.divisionTable);

        setDivisionTable();
    }

    private void setDivisionTable(){
        try{
            for(int i = 0; i < teamList.size(); i++){
                final JSONObject team = teamList.get(i);
                TableRow tRow = new TableRow(this);

                Button name_button = new Button(this);
                final String teamName = team.getString("name");
                name_button.setId(1000 + i);
                name_button.setText(teamName);
                name_button.setBackgroundResource(R.drawable.border);
                name_button.setPadding(2, 0, 2, 0);
                name_button.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        Intent intent = new Intent(DisplayDivision.this, DisplayTeamActivity.class);
                        intent.putExtra("TEAM_INFO", team.toString());
                        startActivity(intent);
                    }
                });

                tRow.addView(name_button);

                divTable.addView(tRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
}
