package com.example.nhlapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class DisplayPerson extends AppCompatActivity {
    final int CODE_GET_REQUEST = 1024;
    TextView name, team, posNum;
    JSONObject playerInfo;
    JSONObject playerStats;
    TableLayout statTable;
    String playerGP, playerGoals, playerAssists, playerPoints, playerTOI, playerID;

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

            playerID = person.getString("id");
            JSONObject currTeam = person.getJSONObject("currentTeam");
            String currTeamName = currTeam.getString("name") + " | ";
            team.setText(currTeamName);

            String playerNum = person.getString("primaryNumber");
            JSONObject position = person.getJSONObject("primaryPosition");
            String playerPos = position.getString("name");
            String playerPosNum = playerPos + " | " + playerNum;
            posNum.setText(playerPosNum);

            playerStats = new JSONObject(getIntent().getStringExtra("PLAYER_STATS"));

            setPlayerStats();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setPlayerStats() {
        for (int i = 2019; i > 2004; i--) {

            playerGP = "0";
            playerTOI = "00:00";
            playerGoals = "0";
            playerAssists = "0";
            playerPoints = "0";

            String urlYear = (i-1) + "" + i;

            if (i != 2019) {
                PerformNetworkRequest pnr = new PerformNetworkRequest(API.URL_READ_INDIVIDUAL
                        + playerID + API.URL_STAT_LINK + urlYear, null, CODE_GET_REQUEST);
                try {
                    playerStats = new JSONObject(pnr.execute().get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

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
                e.printStackTrace();
            }

            TableRow tRow = new TableRow(this);
            Button season_label = new Button(this);
            season_label.setId(200 + 1);
            String seasonYear = (i - 1) + "-" + i;
            season_label.setText(seasonYear);
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

            statTable.addView(tRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    class PerformNetworkRequest extends AsyncTask<Void, Void, String> {

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
