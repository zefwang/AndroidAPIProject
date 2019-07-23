package com.example.nhlapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class DisplayPerson extends AppCompatActivity {
    TextView name, team, posNum;
    JSONObject playerInfo;
    JSONObject playerStats;
    TableLayout statTable;
    String playerGP, playerGoals, playerAssists, playerPoints, playerTOI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_person);

        name = (TextView) findViewById(R.id.playerName);
        team = (TextView) findViewById(R.id.playerTeam);
        posNum = (TextView) findViewById(R.id.playerPosNum);
        statTable = (TableLayout) findViewById(R.id.indivTable);

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

            playerStats = new JSONObject(getIntent().getStringExtra("PLAYER_STATS"));

            setPlayerStats(playerStats);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setPlayerStats(JSONObject playerStats) {
        try {
            JSONObject stats = (JSONObject) playerStats.getJSONArray("stats").get(0);
            stats = (JSONObject) stats.getJSONArray("splits").get(0);
            JSONObject individualStats = stats.getJSONObject("stat");
            this.playerGP = individualStats.getString("games");
            this.playerTOI = individualStats.getString("timeOnIcePerGame");
            this.playerGoals = individualStats.getString("goals");
            this.playerAssists = individualStats.getString("assists");
            this.playerPoints = individualStats.getString("points");
        } catch (JSONException e) {
            playerGoals = "0";
            playerAssists = "0";
            playerPoints = "0";
        }

        TableRow tRow = new TableRow(this);

        Button season_label = new Button(this);
        season_label.setId(200 + 1);
        season_label.setText("2018-2019");
        season_label.setBackgroundResource(R.drawable.border);
        season_label.setPadding(2, 0, 2, 0);
        season_label.setGravity(Gravity.CENTER);
        tRow.addView(season_label);

        Button gp_label = new Button(this);
        gp_label.setId(200 + 1);
        gp_label.setText(playerGP);
        gp_label.setBackgroundResource(R.drawable.border);
        gp_label.setPadding(2, 0, 2, 0);
        gp_label.setGravity(Gravity.CENTER);
        tRow.addView(gp_label);

        Button goals_label = new Button(this);
        goals_label.setId(300 + 1);
        goals_label.setText(playerGoals);
        goals_label.setBackgroundResource(R.drawable.border);
        goals_label.setPadding(2, 0, 2, 0);
        goals_label.setGravity(Gravity.CENTER);
        tRow.addView(goals_label);

        Button assists_label = new Button(this);
        assists_label.setId(400 + 1);
        assists_label.setText(playerAssists);
        assists_label.setBackgroundResource(R.drawable.border);
        assists_label.setPadding(2, 0, 2, 0);
        assists_label.setGravity(Gravity.CENTER);
        tRow.addView(assists_label);

        Button points_label = new Button(this);
        points_label.setId(500 + 1);
        points_label.setText(playerPoints);
        points_label.setBackgroundResource(R.drawable.border);
        points_label.setPadding(2, 0, 2, 0);
        points_label.setGravity(Gravity.CENTER);
        tRow.addView(points_label);

        Button time_label = new Button(this);
        time_label.setId(600 + 1);
        time_label.setText(playerTOI);
        time_label.setBackgroundResource(R.drawable.border);
        time_label.setPadding(2, 0, 2, 0);
        time_label.setGravity(Gravity.CENTER);
        tRow.addView(time_label);

        statTable.addView(tRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
    }
}
