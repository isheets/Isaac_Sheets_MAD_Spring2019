package com.example.lab09;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    Button newQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newQuestion = findViewById(R.id.newQuestion);
        newQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchQuestion().execute();
            }
        });
    }

    static class FetchQuestion extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            //do stuff like reset textviews, show progressbar, etc
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
                JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
                //TODO: populate questions and options(random) (decode strings before displaying), keep track of correct (maybe look at swift logic)


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}


