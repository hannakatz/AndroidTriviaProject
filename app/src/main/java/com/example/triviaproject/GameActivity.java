package com.example.triviaproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private int presCounter = 0;
    private int maxPresCounter = 4;
    private String[] keys = {"R", "I", "B", "D", "X", "X", "X", "X", "X", "X", "X"};
    private String textAnswer = "BIRD";
    private Player player;
    TextView textScreen, textQuestion, textTitle, setScore;
    Animation animation, shakeAnimation, animationSmallToBig, aniFade;
    private static int score;
    private static int lives = 3;
    private static int counterProgress = 0;
    private static boolean newGame = false;
    private ImageView live1, live2, live3 , hurryUp, statusImage;
    private Button btnBack;
    ProgressBar progressBar;
    CountDownTimer mCountDownTimer;

    private final String CREDENTIAL_SHARED_PREF = "our_shared_pref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        animation = AnimationUtils.loadAnimation(this, R.anim.smallbigforth);
        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);
        animationSmallToBig = AnimationUtils.loadAnimation(this, R.anim.smalltobig);
        aniFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadein);

        btnBack = findViewById(R.id.btn_back);

        live1 = findViewById(R.id.live_1);
        live2 = findViewById(R.id.live_2);
        live3 = findViewById(R.id.live_3);
        hurryUp = findViewById(R.id.hurry_up);
        statusImage = findViewById(R.id.status_image);

        setScore = findViewById(R.id.text_count);

        mCountDownTimer = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                counterProgress++;
                progressBar.setProgress((int)counterProgress*100/(30000/1000));
                if(counterProgress == 22){
                    Drawable progressDrawableRed = getResources().getDrawable(R.drawable.progress_red);
                    progressBar.setProgressDrawable(progressDrawableRed);
                    hurryUp.startAnimation(shakeAnimation);
                }
                if(counterProgress == 25){
                    hurryUp.startAnimation(aniFade);
                }
                if (newGame == true) {
                    counterProgress = 1;
                    progressBar.setProgress(counterProgress);
                    mCountDownTimer.start();
                    newGame = false;
                }

            }

            @Override
            public void onFinish() {
                setHurt();
            }
        };
        mCountDownTimer.start();
        setLetters();

        SharedPreferences credentials = getSharedPreferences(CREDENTIAL_SHARED_PREF, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = credentials.getString("Player","");
        player = gson.fromJson(json,Player.class);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameActivity.this.finish();
            }
        });


    }

    private String[] shuffleArray(String[] array){
        Random random = new Random();
        for(int i = array.length-1 ; i>0 ; i--){
            int index = random.nextInt(i+1);
            String a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
        return array;
    }
    private void addView(LinearLayout viewParent, final String text, final EditText editText){
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        linearLayoutParams.rightMargin = 15;
        final TextView textView = new TextView(this);
        textView.setLayoutParams(linearLayoutParams);
        textView.setBackgroundResource(R.drawable.bgpink);
        textView.setTextColor(this.getResources().getColor(R.color.purple));
        textView.setGravity(Gravity.CENTER);
        textView.setText(text);
        textView.setTextSize(32);
        textView.setClickable(true);
        textView.setFocusable(true);

        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/FredokaOneRegular.ttf");

        textQuestion = findViewById(R.id.text_question);
        textScreen = findViewById(R.id.text_screen);
        textTitle = findViewById(R.id.text_title);

        textQuestion.setTypeface(typeface);
        textScreen.setTypeface(typeface);
        textTitle.setTypeface(typeface);
        editText.setTypeface(typeface);
        textView.setTypeface(typeface);
        progressBar = findViewById(R.id.progressbar);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (presCounter < maxPresCounter) {
                    if (presCounter == 0) {
                        editText.setText("");
                    }
                    editText.setText(editText.getText().toString() + text);
                    textView.startAnimation(animation);
                    textView.animate().alpha(0).setDuration(300);
                    presCounter++;

                    if (presCounter == maxPresCounter) {
                        doValidate();
                    }

                }
            }
        });
        viewParent.addView(textView);
    }

    private void doValidate(){
        presCounter = 0;
        EditText editText = findViewById(R.id.edit_text);
        final LinearLayout linearLayout1 = findViewById(R.id.layout_parent1);
        final LinearLayout linearLayout2 = findViewById(R.id.layout_parent2);
        final LinearLayout linearLayout3 = findViewById(R.id.layout_parent3);

        if(editText.getText().toString().equals(textAnswer)){
            Toast.makeText(GameActivity.this, "Correct", Toast.LENGTH_SHORT).show();
            score++;
            if(score == 3){
                setScore();
                statusImage.setBackgroundResource(R.drawable.won);
                statusImage.startAnimation(animationSmallToBig);
                player.setHighScore(score);
                statusImage.startAnimation(aniFade);
                finish();
            }
            String scoreString = Integer.toString(score);
            setScore.setText(scoreString);
            newGame = true;
        }
        else {
            Toast.makeText(GameActivity.this, "Wrong", Toast.LENGTH_SHORT).show();
            setHurt();
        }
        editText.setText("");

        keys = shuffleArray(keys);
        linearLayout1.removeAllViews();
        linearLayout2.removeAllViews();
        linearLayout3.removeAllViews();
        for(String key : keys){
            setLetters();
        }
    }

    private void setScore(){
        player.playerScores.add(new PlayerScore(score, "Survival Mode"));

        SharedPreferences credentials = getSharedPreferences(CREDENTIAL_SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = credentials.edit();
        Gson gson = new Gson();
        String json = gson.toJson(player);
        editor.putString("Player",json);
        editor.commit();
    }

    private void setHurt(){
        if(lives == 3){
            live3.setBackgroundResource(R.color.transparent);
        }
        if(lives == 2){
            live2.setBackgroundResource(R.color.transparent);
        }
        if(lives == 1){
            player.setHighScore(score);
            live1.setBackgroundResource(R.color.transparent);
            statusImage.setBackgroundResource(R.drawable.gameover);
            statusImage.startAnimation(animationSmallToBig);
            statusImage.startAnimation(aniFade);
            finish();
        }
        lives--;
    }

    private void setLetters(){
        int x = 0;
        keys = shuffleArray(keys);
        for(String key : keys){

            if(x < 6){
                addView(((LinearLayout)findViewById(R.id.layout_parent1)), key, ((EditText) findViewById(R.id.edit_text)));
            }
            else if(x > 5 && x < 12){
                addView(((LinearLayout)findViewById(R.id.layout_parent2)), key, ((EditText) findViewById(R.id.edit_text)));
            }
            else {
                addView(((LinearLayout)findViewById(R.id.layout_parent3)), key, ((EditText) findViewById(R.id.edit_text)));
            }
            x++;
        }
        maxPresCounter = textAnswer.length();
    }


}