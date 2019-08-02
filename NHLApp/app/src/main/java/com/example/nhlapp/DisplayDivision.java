package com.example.nhlapp;

import android.content.Intent;
import android.os.AsyncTask;
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
import java.util.HashMap;
import java.util.List;

public class DisplayDivision extends AppCompatActivity {
    final int CODE_GET_REQUEST = 1024;
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
            //e.printStackTrace();
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
                        intent.putExtra("DIVISION_TEAMS", divTeams.toString());
                        startActivity(intent);
                    }
                });
                tRow.addView(name_button);

                PerformNetworkRequest request = new PerformNetworkRequest(API.URL_READ_TEAMS + "/" + team.getString("id")
                        + "/stats", null, CODE_GET_REQUEST);

                final String result = request.execute().get();

                JSONObject teamStats = (JSONObject)new JSONObject(result).getJSONArray("stats").get(0);
                teamStats = teamStats.getJSONArray("splits").getJSONObject(0).getJSONObject("stat");

                Button wins_button = new Button(this);
                wins_button.setId(2000+i);
                wins_button.setText(teamStats.getString("wins"));
                wins_button.setBackgroundResource(R.drawable.border);
                wins_button.setPadding(2, 0, 2, 0);
                tRow.addView(wins_button);

                Button loss_button = new Button(this);
                loss_button.setId(3000+i);
                loss_button.setText(teamStats.getString("losses"));
                loss_button.setBackgroundResource(R.drawable.border);
                loss_button.setPadding(2, 0, 2, 0);
                tRow.addView(loss_button);

                Button otloss_button = new Button(this);
                otloss_button.setId(2000+i);
                otloss_button.setText(teamStats.getString("ot"));
                otloss_button.setBackgroundResource(R.drawable.border);
                otloss_button.setPadding(2, 0, 2, 0);
                tRow.addView(otloss_button);

                Button points_button = new Button(this);
                points_button.setId(2000+i);
                points_button.setText(teamStats.getString("pts"));
                points_button.setBackgroundResource(R.drawable.border);
                points_button.setPadding(2, 0, 2, 0);
                tRow.addView(points_button);

                divTable.addView(tRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    class PerformNetworkRequest extends AsyncTask<Void, Void, String>{
        String url;
        HashMap<String, String> params;
        int requestCode;

        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //this method will give the response from the request
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        //the network operation will be performed in background
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }
}
