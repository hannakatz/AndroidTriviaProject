package com.example.triviaproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class WinActivity extends AppCompatActivity {
    ImageView winImage;
    Button startNewGameBtn;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        winImage = findViewById(R.id.won_image);
        startNewGameBtn = findViewById(R.id.new_game_btn);

        animation = AnimationUtils.loadAnimation(this, R.anim.smalltobig);
        winImage.startAnimation(animation);



    }
}