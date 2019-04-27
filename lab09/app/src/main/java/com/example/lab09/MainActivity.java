package com.example.lab09;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

     Button newQuestion;
     Button option1;
     Button option2;
     Button option3;
     Button option4;
     List<Button> buttonList = new ArrayList<>();

     TextView questionText;


     List<String> options = new ArrayList<>();
     String correctAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //listen for user to answer
        option1 = findViewById(R.id.option1);
        option1.setOnClickListener(this);
        option2 = findViewById(R.id.option2);
        option2.setOnClickListener(this);
        option3 = findViewById(R.id.option3);
        option3.setOnClickListener(this);
        option4 = findViewById(R.id.option4);
        option4.setOnClickListener(this);

        //add buttons to arraylist
        buttonList.add(option1);
        buttonList.add(option2);
        buttonList.add(option3);
        buttonList.add(option4);

        for(Button curButt : buttonList) {
            curButt.setAlpha(0);
        }

        //get the question textView
        questionText = findViewById(R.id.questionText);

        //listen for new question request
        newQuestion = findViewById(R.id.newQuestion);
        newQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchQuestion().execute();
            }
        });
    }

    public void checkAnswer(Button choice) {
        //answer correct
        if(choice.getText().equals(correctAnswer)) {
            //disable buttons, show next question button, highlight selection
            for(Button curButt : buttonList) {
                curButt.setEnabled(false);
                if(curButt == choice) {
                    choice.setBackgroundColor(getResources().getColor(R.color.green));
                }
                else {
                    curButt.setAlpha((float) 0.5);
                }
            }

        }
        else { //answer incorrect
            //show selection, highlight correct answer, disable buttons
            for(Button curButt : buttonList) {
                curButt.setEnabled(false);
                if(curButt == choice) {
                    curButt.setBackgroundColor(getResources().getColor(R.color.red));
                }
                else if(curButt.getText().equals(correctAnswer)) {
                    curButt.setBackgroundColor(getResources().getColor(R.color.green));
                    curButt.setAlpha((float) 0.75);
                }
                else {
                    curButt.setAlpha((float) 0.5);
                }
            }
        }
        newQuestion.setAlpha(1);
    }

    @Override
    public void onClick(View v) {
        checkAnswer((Button) v);
    }

    class FetchQuestion extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            //do stuff like reset textviews, show progressbar, etc
            for(Button curButt : buttonList) {
                curButt.setAlpha(0);
                questionText.setAlpha(0);
            }
            newQuestion.setAlpha(0);
            correctAnswer = "";
            options.removeAll(options);
        }

        @Override
        protected String doInBackground(String... strings) {
            //make the request
            try {
                URL url = new URL("https://opentdb.com/api.php?amount=1&category=9&type=multiple&encode=url3986");
                //open connection
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                try {
                    //create an InputStreamReader with the JSON stream
                    InputStreamReader is = new InputStreamReader(urlConnection.getInputStream());
                    //convert the byte stream to a character stream using BufferedReader
                    BufferedReader bufferedReader = new BufferedReader(is);
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    //loop through the character stream and build a sequence of characters using StringBuilder
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    //convert character sequence to a String
                    return stringBuilder.toString();
                } finally {
                    //disconnect the http connection
                    urlConnection.disconnect();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        protected void onPostExecute(String response) {
            //process the response

            //check response
            if(response == null) {
                response = "ERROR";
            }
            String result;
            try {
                 result = java.net.URLDecoder.decode(response, StandardCharsets.UTF_8.name());
                 Log.d("debug", "decoded result: " + result);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            try {
                JSONObject responseObject = (JSONObject) new JSONTokener(response).nextValue();
                //TODO: populate questions and options(random) (decode strings before displaying), keep track of correct (maybe look at swift logic)
                Log.d("debug", responseObject.toString(4));

                //get the question
                String question = responseObject.getJSONArray("results").getJSONObject(0).getString("question");
                //decode it
                question = java.net.URLDecoder.decode(question, StandardCharsets.UTF_8.name());
                //set the text
                questionText.setText(question);
                questionText.setAlpha(1);

                //get the options
                correctAnswer = responseObject.getJSONArray("results").getJSONObject(0).getString("correct_answer");
                correctAnswer = java.net.URLDecoder.decode(correctAnswer, StandardCharsets.UTF_8.name());
                options.add(correctAnswer);

                JSONArray incorrectAnswers = responseObject.getJSONArray("results").getJSONObject(0).getJSONArray("incorrect_answers");

                for(int i = 0; i < incorrectAnswers.length(); i++) {
                    String curAnswer = incorrectAnswers.get(i).toString();
                    curAnswer = java.net.URLDecoder.decode(curAnswer, StandardCharsets.UTF_8.name());
                    options.add(curAnswer);
                }

                //randomize options
                Collections.shuffle(options);

                //set the text for option buttons
                option1.setText(options.get(0));
                option2.setText(options.get(1));
                option3.setText(options.get(2));
                option4.setText(options.get(3));

                //show the buttons
                for(Button curButt : buttonList) {
                    curButt.setEnabled(true);
                    curButt.setBackgroundColor(getResources().getColor(R.color.gray));
                    curButt.setAlpha(1);
                }

                Log.d("debug", "question: " + question);
                Log.d("debug", "options" + options);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}


