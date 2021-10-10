package com.example.triviaproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class WinActivity extends AppCompatActivity {
    ImageView view;
    Animation animationSmallToBig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);
        view = findViewById(R.id.gameover_image);
        animationSmallToBig = AnimationUtils.loadAnimation(this, R.anim.smalltobig);
        view.startAnimation(animationSmallToBig);
    }
}