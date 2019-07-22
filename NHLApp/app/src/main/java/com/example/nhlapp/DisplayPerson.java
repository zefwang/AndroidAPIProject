package com.example.nhlapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class DisplayPerson extends AppCompatActivity {
    TextView name, team, posNum;
    JSONObject playerInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_person);

        name = (TextView) findViewById(R.id.playerName);
        team = (TextView) findViewById(R.id.playerTeam);
        posNum = (TextView) findViewById(R.id.playerPosNum);

        String playerName = getIntent().getStringExtra("PLAYER_NAME");
        name.setText(playerName);

        try {
            playerInfo = new JSONObject(getIntent().getStringExtra("PLAYER_INFO"));


            JSONObject person = (JSONObject) playerInfo.getJSONArray("people").get(0);

            JSONObject currTeam = person.getJSONObject("currentTeam");
            String currTeamName = currTeam.getString("name") + " | ";
            team.setText(currTeamName);

            String playerNum = person.getString("primaryNumber");
            JSONObject position = person.getJSONObject("primaryPosition");
            String playerPos = position.getString("name");
            String playerPosNum = playerPos + " | " + playerNum;
            posNum.setText(playerPosNum);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
}
