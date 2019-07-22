package com.example.nhlapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONObject;

public class DisplayPerson extends AppCompatActivity {
    TextView name;
    JSONObject playerInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_person);

        name = (TextView) findViewById(R.id.playerName);
        String playerName = getIntent().getStringExtra("PLAYER_NAME");
        name.setText(playerName);

        System.out.println(getIntent().getStringExtra("PLAYER_INFO"));
    }
}
