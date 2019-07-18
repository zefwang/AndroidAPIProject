package com.example.nhlapp;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DisplayTeamActivity extends AppCompatActivity {
    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    JSONObject jsonObject;
    TextView teamName;
    TextView teamVenue;
    TextView teamDivConf;
    TableLayout teamRoster;
    JSONArray fullRoster;
    boolean execIndividual;
    String playerGP, playerGoals, playerAssists, playerPoints, playerTOI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_team);

        teamName = (TextView) findViewById(R.id.team_name);
        teamVenue = (TextView) findViewById(R.id.team_venue);
        teamDivConf = (TextView) findViewById(R.id.team_div_conf);
        teamRoster = (TableLayout) findViewById(R.id.rosterTable);

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


            PerformNetworkRequest request = new PerformNetworkRequest(API.URL_READ_TEAMS + "/" + jsonObject.getString("id") + "?expand=team.roster", null, CODE_GET_REQUEST);
            request.execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void setRoster(JSONArray teamRosterArray) {
        for (int i = 0; i < teamRosterArray.length(); i++) {
            playerGP = "0";
            playerGoals = "0";
            playerAssists = "0";
            playerPoints = "0";
            playerTOI = "0";

            TableRow tRow = new TableRow(this);
            if (i % 2 != 0)
                tRow.setBackgroundColor(Color.GRAY);
            tRow.setId(i + 1);
            tRow.setBackgroundResource(R.drawable.border);
            tRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            try {
                TextView name_label = new TextView(this);

                JSONObject nameObj = teamRosterArray.getJSONObject(i);
                JSONObject personObj = nameObj.getJSONObject("person");
                String name = personObj.getString("fullName");
                String playerID = personObj.getString("id");

                name_label.setId(100 + i);
                name_label.setText(name);
                name_label.setBackgroundResource(R.drawable.border);
                name_label.setPadding(2, 0, 2, 0);
                tRow.addView(name_label);


                PerformNetworkRequest pnr = new PerformNetworkRequest("https://statsapi.web.nhl.com/api/v1/people/" + playerID
                        + "/stats?stats=statsSingleSeason&season=20182019", null, CODE_GET_REQUEST);
                String result = pnr.execute().get();

                parseIndividualData(result);

                if (execIndividual) {
                    tRow = this.addRow(i, tRow);
                    System.out.println("Executed row properly");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            teamRoster.addView(tRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    private void parseIndividualData(String result) {
        try {
            JSONObject object = new JSONObject(result);
            JSONObject stats = (JSONObject) object.getJSONArray("stats").get(0);
            //System.out.println(stats.toString());
            stats = (JSONObject) stats.getJSONArray("splits").get(0);
            //System.out.println(stats.toString());
            JSONObject individualStats = stats.getJSONObject("stat");
            execIndividual = true;
            setPlayerInfo(individualStats);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private TableRow addRow(int id, TableRow tRow) {
        TextView gp_label = new TextView(this);
        gp_label.setId(200 + id);
//                System.out.println("GP: " + playerGP);
        gp_label.setText(playerGP);
        gp_label.setBackgroundResource(R.drawable.border);
        gp_label.setPadding(2, 0, 2, 0);
        gp_label.setGravity(Gravity.CENTER);
        tRow.addView(gp_label);

        TextView goals_label = new TextView(this);
        goals_label.setId(300 + id);
//                System.out.println("Points: " + playerGoals);
        goals_label.setText(playerGoals);
        goals_label.setBackgroundResource(R.drawable.border);
        goals_label.setPadding(2, 0, 2, 0);
        goals_label.setGravity(Gravity.CENTER);
        tRow.addView(goals_label);

        TextView assists_label = new TextView(this);
        assists_label.setId(400 + id);
//                System.out.println("Assists: " + playerAssists);
        assists_label.setText(playerAssists);
        assists_label.setBackgroundResource(R.drawable.border);
        assists_label.setPadding(2, 0, 2, 0);
        assists_label.setGravity(Gravity.CENTER);
        tRow.addView(assists_label);

        TextView points_label = new TextView(this);
        points_label.setId(500 + id);
//                System.out.println("Points: " + playerPoints);
        points_label.setText(playerPoints);
        points_label.setBackgroundResource(R.drawable.border);
        points_label.setPadding(2, 0, 2, 0);
        points_label.setGravity(Gravity.CENTER);
        tRow.addView(points_label);

        TextView time_label = new TextView(this);
        time_label.setId(600 + id);
//                System.out.println("TOI: " + playerTOI);
        time_label.setText(playerTOI);
        time_label.setBackgroundResource(R.drawable.border);
        time_label.setPadding(2, 0, 2, 0);
        time_label.setGravity(Gravity.CENTER);
        tRow.addView(time_label);

        return tRow;
    }

    void setPlayerInfo(JSONObject individualStats) {
        try {
            this.playerGP = individualStats.getString("games");
            this.playerTOI = individualStats.getString("timeOnIcePerGame");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            this.playerGoals = individualStats.getString("goals");
            this.playerAssists = individualStats.getString("assists");
            this.playerPoints = individualStats.getString("points");
        } catch (JSONException e) {
            System.out.println("Goalie reached.");
            playerGoals = "0";
            playerAssists = "0";
            playerPoints = "0";
        }
        System.out.println("Games: " + playerGP + "\nTOI: " + playerTOI + "\nGoals: " + playerGoals + "\nAssists: " + playerAssists + "\nPoints: " + playerPoints);
    }

    //inner class to perform network request extending an AsyncTask
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
            try {
                JSONObject object = new JSONObject(s);
                fullRoster = object.getJSONArray("teams");
                JSONObject roster = (JSONObject) fullRoster.get(0);
                fullRoster = roster.getJSONObject("roster").getJSONArray("roster");
                execIndividual = false;
                setRoster(fullRoster);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //the network operation will be performed in background
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }
}
