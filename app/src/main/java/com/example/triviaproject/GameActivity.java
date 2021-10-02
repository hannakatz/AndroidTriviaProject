package com.example.triviaproject;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private int presCounter = 0;
    private int maxPresCounter = 4;
    private String[] keys = {"R", "I", "B", "D", "X"};
    private String textAnswer = "BIRD";
    TextView textScreen, textQuestion, textTitle;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        animation = AnimationUtils.loadAnimation(this, R.anim.smallbigforth);

        keys = shuffleArray(keys);
        for(String key : keys){
            addView(((LinearLayout)findViewById(R.id.layout_parent)), key, ((EditText) findViewById(R.id.edit_text)));
        }
        maxPresCounter = 4;
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
        linearLayoutParams.rightMargin = 30;
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
        LinearLayout linearLayout = findViewById(R.id.layout_parent);

        if(editText.getText().toString().equals(textAnswer)){
            Toast.makeText(GameActivity.this, "Correct", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(GameActivity.this, "Wrong", Toast.LENGTH_SHORT).show();
        }
        editText.setText("");

        keys = shuffleArray(keys);
        linearLayout.removeAllViews();
        for(String key : keys){
            addView(linearLayout, key, editText);
        }
    }
}