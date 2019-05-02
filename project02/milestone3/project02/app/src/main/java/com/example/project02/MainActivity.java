package com.example.project02;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity {
    //recylerview stuff
    RecyclerView mRV;
    MyAdapter myAdapter;
    GridLayoutManager mGM;

    //data persistence name
    private static final String PREFS_NAME = "TRIVIA_TANGLER";

    //textviews for messaging
    TextView header;
    TextView caption;
    TextView lives;

    //drawable references
    Drawable yellowButt;
    Drawable greenButt;
    Drawable redButt;
    Drawable grayButt;

    //handler for delays
    final Handler handler = new Handler();


    List<Integer> buttIdList = new ArrayList<>(); //in order for tagging the buttons
    public static List<Button> buttList = new ArrayList<>();
    List<Integer> sequenceIdList = new ArrayList<>(); //this is the shuffled version

    //main action button
    Button actionButt;

    int clickNum = 0; //start at zero, increment each click, reset each level/life

    //current level
    int level = 1;
    //current num lives
    int numLives = 3;
    int subLevel = 1;

    //hold all answer options
    List<String> options = new ArrayList<>();
    //store correct answer
    String correctAnswer;
    //store question
    String question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //animate activity switch
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        setContentView(R.layout.activity_main);

        //set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //load data if possible
        loadData(this);

        //get those drawables
        yellowButt = getDrawable(R.drawable.yellow_butt);
        greenButt = getDrawable(R.drawable.green_butt);
        redButt = getDrawable(R.drawable.red_butt);
        grayButt = getDrawable(R.drawable.gray_butt);

        //get button ref
        actionButt = findViewById(R.id.actionButt);

        //get recyclerview ref
        mRV = findViewById(R.id.recycler);

        //get textview references
        header = findViewById(R.id.heading);
        caption = findViewById(R.id.caption);
        lives = findViewById(R.id.lives);

        //set initial texts based on new game or resume game
        if (level == 1) { //new game
            header.setText("Welcome!");
            caption.setText("Don't get tangled!");
            lives.setText("Lives: " + numLives);
            actionButt.setText("Start Game");
        } else { //resume game
            header.setText("Welcome back!");
            caption.setText("Don't get tangled!");
            lives.setText("Lives: " + numLives);
            actionButt.setText("Resume Level " + level);
        }

        //get a question to start with
        new FetchQuestion().execute();

        //init span to 1, change based on level (up to 4)
        mGM = new GridLayoutManager(MainActivity.this, level + 1, GridLayoutManager.VERTICAL, false);
        mRV.setLayoutManager(mGM);

        //populate list based on level
        if (level <= 1) {
            level = 1;
            for (int i = 0; i < 4; i++) {
                buttIdList.add(i);
            }
        } else {
            for (int i = 0; i < 4; i++) {
                buttIdList.add(i);
            }
            for (int i = level * level; i < (level + 1) * (level + 1); i++) {
                buttIdList.add(i);
            }
        }

        //pass id list and set adapter
        myAdapter = new MyAdapter(MainActivity.this, buttIdList);
        mRV.setAdapter(myAdapter);

        //this needs to be called whenever we add or subtract
        myAdapter.setOnItemClickListener(onItemClickListener);

        //set action button game listener
        actionButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get action button text
                String curText = ((Button) v).getText().toString();
                //normalize it
                curText = curText.toLowerCase();

                if (curText.equals("start game")) {
                    newGame();
                } else if (curText.equals("resume level " + level)) {
                    newGame();
                } else if (curText.equals("retry")) {
                    restartLevel();
                } else if (curText.equals("go back a level")) {
                    prevLevel();
                }
            }
        });

    }

    //initial game start up
    private void newGame() {
        //update messaging
        header.setText("Level " + level);

        //hide action button
        actionButt.setVisibility(View.INVISIBLE);

        //new sequence
        newSequence();

        //play sequence
        playSequence();
    }

    //update messaging, play sequence
    private void nextLevel() {
        //get a new question
        new FetchQuestion().execute();
        //increment level
        level++;
        //add new IDs to list
        for (int i = level * level; i < (level + 1) * (level + 1); i++) {
            buttIdList.add(i);
        }
        mGM.setSpanCount(level + 1);
        Log.d("debuglog", "ButtIdList after adding:" + buttIdList);
        //tell adapter about new id so we can get another button
        myAdapter.updateList(buttIdList);
        myAdapter.notifyDataSetChanged();

        //reset click num and sub level
        clickNum = 0;
        subLevel = 1;

        //reset num lives
        numLives = 3;

        //disable the buttons
        for (Button curButt : buttList) {
            curButt.setBackground(yellowButt);
            curButt.setEnabled(false);
        }

        //messaging
        header.setText("Level " + level);
        actionButt.setVisibility(View.INVISIBLE);
        mRV.setVisibility(View.VISIBLE);
        lives.setVisibility(View.VISIBLE);
        lives.setText("Lives: " + numLives);

        //get new sequence
        newSequence();

        //set text
        caption.setText("Get ready...");

        //play it
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                playSequence();
            }
        }, 1000);

    }

    private void nextSubLevel() {
        //increment sublevel
        subLevel++;
        //reset click num
        clickNum = 0;
        //get new sequence
        newSequence();
        playSequence();
    }

    private void prevLevel() {

        //get a new question
        new FetchQuestion().execute();
        //increment level
        level--;

        //empty id list
        buttIdList.removeAll(buttIdList);
        //regenerate appropriately
        if (level <= 1) {
            level = 1;
            for (int i = 0; i < 4; i++) {
                buttIdList.add(i);
            }
        } else {
            for (int i = 0; i < 4; i++) {
                buttIdList.add(i);
            }
            for (int i = level * level; i < (level + 1) * (level + 1); i++) {
                buttIdList.add(i);
            }
        }

        Log.d("debuglog", "ButtIdList after removal: " + String.valueOf(buttIdList));
        Log.d("debuglog", "ButtList size after removal: " + buttList.size());

        //set the right span count
        mGM.setSpanCount(level + 1);

        //tell adapter about new id so we can get another button
        myAdapter.updateList(buttIdList);
        myAdapter.notifyDataSetChanged();

        //reset click num and sublevel
        clickNum = 0;
        subLevel = 1;

        //reset num lives
        numLives = 3;

        //messaging
        header.setText("Level " + level);
        header.setTextColor(getResources().getColor(R.color.white));
        caption.setText("Replicate the sequence.");
        actionButt.setVisibility(View.INVISIBLE);
        mRV.setVisibility(View.VISIBLE);
        lives.setVisibility(View.VISIBLE);
        lives.setText("Lives: " + numLives);

        //get new sequence
        newSequence();

        //play sequence
        playSequence();

    }

    private void outOfLives() {
        actionButt.setBackground(redButt);
        actionButt.setText("Go back a level");
        actionButt.setVisibility(View.VISIBLE);
        header.setText("Oops!");
        header.setTextColor(getResources().getColor(R.color.red));
        caption.setText("You ran out of lives on level " + level);
        lives.setText("Lives: " + numLives);
    }

    private void restartLevel() {
        //hide button
        actionButt.setVisibility(View.INVISIBLE);
        //update messaging
        header.setText("Level " + level);
        caption.setText("Replicate the sequence.");
        mRV.setVisibility(View.VISIBLE);
        lives.setText("Lives: " + numLives);
        lives.setVisibility(View.VISIBLE);
        //get a new question
        new FetchQuestion().execute();
        //get a new sequence
        newSequence();
        //play it
        playSequence();
    }

    //launch question activity
    private void showQuestion() {
        //sequence replicated successfully victory sequence! launch question activity
        Intent intent = new Intent(getApplicationContext(), QuestionActivity.class);
        intent.putExtra("level", level);
        intent.putStringArrayListExtra("options", (ArrayList<String>) options);
        intent.putExtra("question", question);
        intent.putExtra("correct", correctAnswer);
        intent.putExtra("lives", numLives);
        startActivityForResult(intent, 1);
    }

    private void playSequence() {
        //reset num clicks
        clickNum = 0;

        //prompt replication
        caption.setText("Replicate the sequence.");

        //disable the buttons
        for (Button curButt : buttList) {
            curButt.setBackground(yellowButt);
            curButt.setAlpha(1);
            curButt.setEnabled(false);
        }

        Log.d("debuglog", "sequenceIdList: " + sequenceIdList);

        for (int i = 0; i < subLevel; i++) {
            //i needs to be final since it's accessed from inner class
            final int finalI = i;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MyAdapter.ButtonViewHolder curView = (MyAdapter.ButtonViewHolder) mRV.findViewHolderForLayoutPosition(sequenceIdList.get(finalI));
                    curView.gridButt.setBackground(greenButt);

                }
            }, 700 + (i * 400));

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MyAdapter.ButtonViewHolder curView = (MyAdapter.ButtonViewHolder) mRV.findViewHolderForLayoutPosition(sequenceIdList.get(finalI));
                    curView.gridButt.setBackground(yellowButt);
                    if (finalI == subLevel - 1) {
                        //enable buttons when done
                        for (Button curButt : buttList) {
                            curButt.setEnabled(true);
                        }
                    }
                }
            }, 1000 + (i * 400));
        }
    }

    //randomize buttIdList
    private void newSequence() {
        sequenceIdList = new ArrayList<>(buttIdList);
        Collections.shuffle(sequenceIdList);
        Log.d("debuglog", "ButtIdList after shuffling: " + buttIdList);
        Log.d("debuglog", "SequenceIdList after shuffling: " + sequenceIdList);
    }

    //listen for grid button clicks
    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int clickTag = viewHolder.getLayoutPosition();
            Log.d("debuglog", "Clicked: " + clickTag);

            if (clickTag == sequenceIdList.get(clickNum)) {
                //correct click! check if it's the last click or just change button color
                view.setBackground(greenButt);
                if (clickNum == (subLevel - 1)) {

                    //turn all buttons green
                    for (Button curButt : buttList) {
                        curButt.setBackground(greenButt);
                    }

                    //increment sublevel or show question
                    if (subLevel == ((level + 1) * (level + 1))) {
                        //turn all buttons green
                        for (Button curButt : buttList) {
                            curButt.setAlpha(.5f);
                        }
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showQuestion();
                            }
                        }, 300);

                    } else {
                        caption.setText("Nice!");
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                nextSubLevel();
                            }
                        }, 1000);
                    }
                } else {

                    clickNum++;
                    Log.d("debuglog", "ClickNum: " + clickNum + " Need: " + (level + 3));

                }
            } else { //bad click! lose life! reset sequence! so on!

                //disable buttons
                for (Button curButt : buttList) {
                    curButt.setEnabled(false);
                    curButt.setBackground(grayButt);
                    curButt.setAlpha(0.5f);
                }

                view.setBackground(redButt);

                MyAdapter.ButtonViewHolder correctView = (MyAdapter.ButtonViewHolder) mRV.findViewHolderForLayoutPosition(sequenceIdList.get(clickNum));
                correctView.gridButt.setBackground(greenButt);
                correctView.gridButt.setAlpha(.85f);

                clickNum = 0;

                //decrement lives
                numLives--;

                //out of lives
                if (numLives == 0) {
                    //revert level
                    outOfLives();
                }

                //still got some lives
                else {
                    //give option to restart
                    header.setText("Wrong sequence!");
                    caption.setText("You lose a life.");
                    lives.setText("Lives: " + numLives);
                    actionButt.setText("Retry");
                    actionButt.setBackground(redButt);
                    actionButt.setVisibility(View.VISIBLE);
                }
            }


        }
    };

    //this is called when we return from questions activity. result code = -1 means wrong answer, result code = 0 means correct answer
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            Log.d("debug", "question correct!");
            nextLevel();
        } else {
            numLives--;
            if (numLives == 0) {
                //revert level (update messaging)
                outOfLives();
            } else {
                restartLevel();
            }
            Log.d("debug", "question incorrect.");
        }
    }

    //get new question from api
    class FetchQuestion extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            //empty out questions/answers
            correctAnswer = "";
            question = "";
            options.removeAll(options);
        }

        @Override
        protected String doInBackground(String... strings) {
            //make the request
            //Log.d("debuglog", "testing doInBG");
            try {
                //Log.d("debuglog", "testing doInBG first try");
                URL url = new URL("https://opentdb.com/api.php?amount=1&category=9&type=multiple&encode=url3986");
                //open connection
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                try {
                    //Log.d("debuglog", "testing doInBG second try");
                    //create an InputStreamReader with the JSON stream
                    InputStreamReader is = new InputStreamReader(urlConnection.getInputStream());
                    //convert the byte stream to a character stream using BufferedReader
                    BufferedReader bufferedReader = new BufferedReader(is);
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    //loop through the character stream and build a sequence of characters using StringBuilder
                    while ((line = bufferedReader.readLine()) != null) {
                        //Log.d("debuglog", "testing doInBG second try in while");
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    //convert character sequence to a String
                    // Log.d("debuglog", "returning");
                    return stringBuilder.toString();
                } finally {
                    //disconnect the http connection
                    urlConnection.disconnect();
                    //Log.d("debuglog", "disconnect");
                }

            } catch (Exception e) {
                e.printStackTrace();
                // Log.d("debuglog", "exception");
            }
            return null;
        }


        protected void onPostExecute(String response) {
            //process the response

            //check response
            if (response == null) {
                response = "ERROR";
            }
            //Log.d("debuglog", response);


            try {
                JSONObject responseObject = (JSONObject) new JSONTokener(response).nextValue();

                //get the question text
                question = responseObject.getJSONArray("results").getJSONObject(0).getString("question");
                //decode it
                question = java.net.URLDecoder.decode(question, StandardCharsets.UTF_8.name());

                //get the options
                correctAnswer = responseObject.getJSONArray("results").getJSONObject(0).getString("correct_answer");
                correctAnswer = java.net.URLDecoder.decode(correctAnswer, StandardCharsets.UTF_8.name());
                options.add(correctAnswer);

                JSONArray incorrectAnswers = responseObject.getJSONArray("results").getJSONObject(0).getJSONArray("incorrect_answers");

                for (int i = 0; i < incorrectAnswers.length(); i++) {
                    String curAnswer = incorrectAnswers.get(i).toString();
                    curAnswer = java.net.URLDecoder.decode(curAnswer, StandardCharsets.UTF_8.name());
                    options.add(curAnswer);
                }

                //randomize options
                Collections.shuffle(options);


                //  Log.d("debug", "question: " + question);
//                Log.d("debug", "options" + options);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    //persist on pause
    @Override
    protected void onPause() {
        super.onPause();
        //save data
        storeData(this);
    }

    public void storeData(Context context) {
        //get access to a shared preferences file
        SharedPreferences sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        //create an editor to write to the shared preferences file
        SharedPreferences.Editor editor = sharedPrefs.edit();
        //add level, lives, and sublevel to the editor
        editor.putInt("level", level);
        editor.putInt("lives", numLives);
        editor.putInt("sublevel", subLevel);
        //commit
        editor.apply();
    }

    public void loadData(Context context) {
        //get access to a shared preferences file
        SharedPreferences sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        int size = sharedPrefs.getInt("size", 0);
        if (sharedPrefs.contains("level") && sharedPrefs.contains("lives") && sharedPrefs.contains("sublevel")) {
            level = sharedPrefs.getInt("level", 1);
            numLives = sharedPrefs.getInt("lives", 3);
            subLevel = sharedPrefs.getInt("sublevel", 1);
        } else {
            Log.d("debuglog", "could not load data from shared prefs");
        }
    }

}



