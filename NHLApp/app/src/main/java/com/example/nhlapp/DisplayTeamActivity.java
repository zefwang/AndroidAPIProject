package com.example.nhlapp;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    JSONObject individualStats;

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

            //Have to access roster of specific team
            int count = 0;

            PerformNetworkRequest request = new PerformNetworkRequest(API.URL_READ_TEAMS + "/" + jsonObject.getString("id") + "?expand=team.roster", null, CODE_GET_REQUEST);
            request.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void setRoster(JSONArray teamRosterArray) {


        for (int i = 0; i < teamRosterArray.length(); i++) {
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
                System.out.println(playerID);

                name_label.setId(100 + i);
                name_label.setText(name);
                name_label.setBackgroundResource(R.drawable.border);
                name_label.setPadding(2, 0, 2, 0);

                tRow.addView(name_label);


                HashMap<String, String> params = new HashMap<>();
                params.put("stats", "statsSingleSeason");
                params.put("season", "20182019");
                PerformNetworkRequest pnr = new PerformNetworkRequest("https://statsapi.web.nhl.com/api/v1/people/" + playerID
                        + "/stats?", params, CODE_GET_REQUEST);
                pnr.execute();

                TextView gp_label = new TextView(this);

                //TODO:
                // Figure out why this isn't working
                // Claims that individualStats is null but has been instantiated already?
//                String gp = individualStats.getString("games");
                gp_label.setId(200 + i);
//                gp_label.setText(gp);
                gp_label.setBackgroundResource(R.drawable.border);
                gp_label.setPadding(2, 0, 2, 0);

                tRow.addView(gp_label);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            teamRoster.addView(tRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }
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

                try {
                    fullRoster = object.getJSONArray("teams");
                    JSONObject roster = (JSONObject) fullRoster.get(0);
                    fullRoster = roster.getJSONObject("roster").getJSONArray("roster");
                    setRoster(fullRoster);
                } catch (JSONException e) {
                    JSONObject stats = (JSONObject) object.getJSONArray("stats").get(0);
                    stats = (JSONObject) stats.getJSONArray("splits").get(0);
                    individualStats = stats.getJSONObject("stat");
                }

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
