package com.example.lab09;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}

class FetchQuestion extends AsyncTask<String, Void, String> {

    protected void onPreExecute() {
        //do stuff like reset textviews, show progressbar, etc
    }

    @Override
    protected String doInBackground(String... strings) {
        //make the request
        return null;
    }


    protected void onPostExecute(String response) {
        //process the response
    }
}
