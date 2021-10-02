package com.example.triviaproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameTypeActivity extends AppCompatActivity {
    Button btnSurvivalMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_type);
        btnSurvivalMode = findViewById(R.id.button_survival_mode);
        btnSurvivalMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameTypeActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });
    }
}