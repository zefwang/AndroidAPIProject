package com.example.nhlapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int CODE_GET_REQUEST = 1024;
    private Spinner teamSpinner, divisionSpinner, conferenceSpinner;
    private Button viewTeam, viewDivision;
    List<String> teamNames;
    private JSONArray fullTeamInfo;
    private String divName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        teamNames = new ArrayList<>();

        teamSpinner = (Spinner) findViewById(R.id.teams_spinner);

        PerformNetworkRequest request = new PerformNetworkRequest(API.URL_READ_TEAMS, null, CODE_GET_REQUEST);

        try {
            request.execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewTeam = (Button) findViewById(R.id.view_selected_team);
        viewTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String spinnerSelection = teamSpinner.getSelectedItem().toString();

                Intent intent = new Intent(MainActivity.this, DisplayTeamActivity.class);
                intent.putExtra("TEAM_INFO", findTeam(spinnerSelection));
                intent.putExtra("DIVISION_TEAMS", findTeams(divName));
                startActivity(intent);
            }
        });

        divisionSpinner = (Spinner) findViewById(R.id.divisions_spinner);
        List<String> divisionNames = new ArrayList<>(Arrays.asList("Metropolitan", "Atlantic", "Central", "Pacific"));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_text_view, divisionNames);
        divisionSpinner.setAdapter(adapter);

        viewDivision = (Button) findViewById(R.id.view_selected_division);
        viewDivision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String spinSelect = divisionSpinner.getSelectedItem().toString();

                Intent intent = new Intent(MainActivity.this, DisplayDivision.class);
                intent.putExtra("DIVISION_NAME", spinSelect);
                intent.putExtra("DIVISION_TEAMS", findTeams(spinSelect));
                startActivity(intent);
            }
        });
    }

    protected String findTeams(String divName) {
        JSONArray teamList = new JSONArray();
        for (int i = 0; i < fullTeamInfo.length(); i++) {
            try {
                JSONObject obj = fullTeamInfo.getJSONObject(i);
                if (obj.getJSONObject("division").getString("name").equals(divName)) {
                    teamList.put(obj);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return teamList.toString();
    }

    private String findTeam(String teamName) {
        for (int i = 0; i < fullTeamInfo.length(); i++) {
            try {
                JSONObject obj = fullTeamInfo.getJSONObject(i);
                if (obj.getString("name").equals(teamName)) {
                    divName = obj.getJSONObject("division").getString("name");
                    return obj.toString();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    private void setTeamList(JSONArray teams) throws JSONException {
        for (int i = 0; i < teams.length(); i++) {
            JSONObject obj = teams.getJSONObject(i);

            teamNames.add(obj.getString("name"));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_text_view, teamNames);
        teamSpinner.setAdapter(adapter);
    }

    //inner class to perform network request extending an AsyncTask
    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {

        //the url where we need to send the request
        String url;

        //the parameters
        HashMap<String, String> params;

        //the request code to define whether it is a GET or POST
        int requestCode;

        //constructor to initialize values
        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        //when the task started displaying a progressbar
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        //this method will give the response from the request
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                fullTeamInfo = object.getJSONArray("teams");
                setTeamList(fullTeamInfo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
