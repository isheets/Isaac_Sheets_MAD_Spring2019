package com.example.project02;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener{

    Button next;
    Button option1;
    Button option2;
    Button option3;
    Button option4;
    List<Button> buttonList = new ArrayList<>();

    TextView questionText;
    TextView headerText;
    TextView captionText;
    TextView livesText;

    //drawable references
    Drawable yellowButt;
    Drawable greenButt;
    Drawable redButt;

    String correctAnswer;

    int level;

    int resultCode = -1; //0 if correct, -1 if false

    int numLives;


    List<String> options = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //animate activity switch
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        setContentView(R.layout.activity_question);

        //set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //get those drawables
        yellowButt = getDrawable(R.drawable.yellow_butt);
        greenButt = getDrawable(R.drawable.green_butt);
        redButt = getDrawable(R.drawable.red_butt);

        //get intent with extras
        Intent intent = getIntent();
        level = intent.getIntExtra("level", 1);
        correctAnswer = intent.getStringExtra("correct");
        Log.d("debuglog", correctAnswer);
        options = intent.getStringArrayListExtra("options");
        String question = intent.getStringExtra("question");
        numLives = intent.getIntExtra("lives", 3);

        //get level text, set text
        headerText = findViewById(R.id.levelText);
        headerText.setText("Checkpoint Reached!");

        //get and set caption
        captionText = findViewById(R.id.caption);
        captionText.setText("Answer question to complete level " + level +".");

        //get and set lives
        livesText = findViewById(R.id.lives);
        livesText.setText("Lives: " + numLives);

        //get buttons, set text, set listener
        option1 = findViewById(R.id.option1);
        option1.setOnClickListener(this);
        option1.setText(options.get(0));
        option2 = findViewById(R.id.option2);
        option2.setText(options.get(1));
        option2.setOnClickListener(this);
        option3 = findViewById(R.id.option3);
        option3.setText(options.get(2));
        option3.setOnClickListener(this);
        option4 = findViewById(R.id.option4);
        option4.setText(options.get(3));
        option4.setOnClickListener(this);

        //add buttons to arraylist
        buttonList.add(option1);
        buttonList.add(option2);
        buttonList.add(option3);
        buttonList.add(option4);

        //get the question textView, set text
        questionText = findViewById(R.id.questionText);
        questionText.setText(question);

        //listen for new question request
        next = findViewById(R.id.next);
        next.setVisibility(View.GONE);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish activity with proper result
                goBack();
            }
        });


    }

    public void goBack() {
        setResult(resultCode);
        finishActivity(1);
        finish();
    }
    public void checkAnswer(Button choice) {
        //answer correct
        questionText.setAlpha(.75f);
        if(choice.getText().equals(correctAnswer)) {
            resultCode = 0;
            //disable buttons, show next button, highlight selection
            for(Button curButt : buttonList) {
                curButt.setEnabled(false);
                if(curButt == choice) {
                    choice.setBackground(greenButt);
                    choice.setAlpha((float) 0.5);
                }
                else {
                    curButt.setAlpha((float) 0.5);
                }
            }
            headerText.setText("Correct!");
            captionText.setText("Level " + level + " complete.");
            headerText.setTextColor(getResources().getColor(R.color.green));
            next.setBackground(greenButt);
            next.setText("Next Level");

        }
        else { //answer incorrect
            //show selection, highlight correct answer, disable buttons
            resultCode = -1;
            for(Button curButt : buttonList) {
                curButt.setEnabled(false);
                if(curButt == choice) {
                    curButt.setBackground(redButt);
                }
                else if(curButt.getText().equals(correctAnswer)) {
                    curButt.setBackground(greenButt);
                    curButt.setAlpha((float) 0.5);
                }
                else {
                    curButt.setAlpha((float) 0.5);
                }
            }
            headerText.setText("Wrong!");
            headerText.setTextColor(getResources().getColor(R.color.red));
            captionText.setText("Try level " + level + " again.");
            livesText.setText("Lives: " + numLives);
            next.setBackground(redButt);
            next.setText("Retry");
        }
        next.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        checkAnswer((Button) v);
    }
}
