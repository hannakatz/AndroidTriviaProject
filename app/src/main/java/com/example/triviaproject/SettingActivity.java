package com.example.triviaproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private Button musicPlayerBtn;
    private String ifMusicPlay;
    private Player player;
    private Gson gson = new Gson();

    private final String CREDENTIAL_SHARED_PREF = "our_shared_pref";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");

        SharedPreferences credentials = getSharedPreferences(CREDENTIAL_SHARED_PREF, Context.MODE_PRIVATE);
        String json = credentials.getString("Player","");
        player = gson.fromJson(json,Player.class);

        musicPlayerBtn = (Button) findViewById(R.id.btn_sound);
        ifMusicPlay = player.musicPlay;

        if (ifMusicPlay.equals("false")) {
            musicPlayerBtn.setBackgroundResource(R.drawable.ic_sound_off);
        } else {
            musicPlayerBtn.setBackgroundResource(R.drawable.ic_sound_play);
        }

        musicPlayerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == musicPlayerBtn) {

            SharedPreferences credentials = getSharedPreferences(CREDENTIAL_SHARED_PREF, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = credentials.edit();

            ifMusicPlay = player.musicPlay;

            if (ifMusicPlay.equals("false")) {
                player.musicPlay = "true";
                musicPlayerBtn.setBackgroundResource(R.drawable.ic_sound_play);
                startService(new Intent(this, MusicService.class));

            } else {
                player.musicPlay = "false";
                musicPlayerBtn.setBackgroundResource(R.drawable.ic_sound_off);
                stopService(new Intent(this, MusicService.class));
            }
            String json = gson.toJson(player);
            editor.putString("Player",json);
            editor.commit();
        }
    }
}





