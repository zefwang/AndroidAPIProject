package com.example.nhlapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class DisplayTeamActivity extends AppCompatActivity {
    JSONObject jsonObject;
    TextView teamName;
    TextView teamVenue;
    TextView teamDivConf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_team);

        teamName = (TextView) findViewById(R.id.team_name);
        teamVenue = (TextView) findViewById(R.id.team_venue);
        teamDivConf = (TextView) findViewById(R.id.team_div_conf);

        try {
            jsonObject = new JSONObject(getIntent().getStringExtra("TEAM_INFO"));
            teamName.setText(jsonObject.getString("name"));

            JSONObject venue = jsonObject.getJSONObject("venue");
            String venueStr = venue.get("name") + ", " + venue.get("city");
            teamVenue.setText(venueStr);

            JSONObject division = jsonObject.getJSONObject("division");
            JSONObject conf = jsonObject.getJSONObject("conference");
            String div_conf = division.get("name") + " || " + conf.get("name");
            teamDivConf.setText(div_conf);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
