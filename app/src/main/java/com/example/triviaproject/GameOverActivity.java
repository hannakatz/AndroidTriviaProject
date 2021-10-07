package com.example.triviaproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class GameOverActivity extends AppCompatActivity {
    ImageView gameOverImage;
    Button startNewGameBtn;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        gameOverImage = findViewById(R.id.game_over_image);
        startNewGameBtn = findViewById(R.id.new_game_btn2);

        animation = AnimationUtils.loadAnimation(this, R.anim.smalltobig);
        gameOverImage.startAnimation(animation);
    }
}