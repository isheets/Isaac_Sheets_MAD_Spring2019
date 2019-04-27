package com.example.project02;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class MainActivity extends AppCompatActivity {
    RecyclerView mRV;

    MyAdapter myAdapter;

    List<Integer> buttIdList = new ArrayList<Integer>(); //in order for tagging the buttons
    List<Button> buttList;
    List<Integer> sequenceIdList = new ArrayList<Integer>(); //this is the shuffled version

    Button actionButt;

    int clickNum = 0 ; //start at zero, increment each click, reset each level/life

    int level = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionButt = findViewById(R.id.actionButt);

        mRV = findViewById(R.id.recycler);

        //init span to 1, change based on level (up to 4)
        GridLayoutManager mGM = new GridLayoutManager(MainActivity.this, 4, GridLayoutManager.VERTICAL, false);

        mRV.setLayoutManager(mGM);

        buttIdList.add(level);
        sequenceIdList = buttIdList;

        myAdapter = new MyAdapter(MainActivity.this, buttIdList);
        mRV.setAdapter(myAdapter);

        //this needs to be called whenever we add or subtract
        myAdapter.setOnItemClickListener(onItemClickListener);

        //set start game listener
        actionButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //eventually we want a function that displays rv and plays sequence
                playSequence();
            }
        });

    }

    private void playSequence() {

        newSequence();

        final Handler handler = new Handler();

        buttList = getButtons();

        for(int i = 0; i < sequenceIdList.size(); i++) {
            //i needs to be final since it's accessed from inner class
            final int finalI = i;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    buttList.get(sequenceIdList.get(finalI)).setBackgroundColor(Color.GREEN);
                }
            }, i*200);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    buttList.get(sequenceIdList.get(finalI)).setBackgroundColor(Color.GRAY);
                }
            }, 200 + (i*200));
        }
    }

    //randomize buttIdList
    private void newSequence() {
        sequenceIdList = buttIdList;
        Collections.shuffle(sequenceIdList);
    }

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int clickTag = viewHolder.getAdapterPosition();

            Log.d("debuglog", "Clicked:" + clickTag);

            if(clickTag == sequenceIdList.get(clickNum)) {
                //correct click! check if it's the last click or just change button color
                if(clickNum == sequenceIdList.size()-1) {
                    //sequence replicated successfully victory sequence! increment level, add to list, notify dataset changed
                    for(Button curButt : buttList) {
                        curButt.setBackgroundColor(Color.GRAY);
                    }
                    level++;
                    buttIdList.add(level);
                    myAdapter.updateList(buttIdList);
                    myAdapter.notifyDataSetChanged();
                    clickNum = 0;

                }
                else {
                    buttList.get(clickTag).setBackgroundColor(Color.GREEN);
                    clickNum++;
                }
            }
            else {
                //bad click! lose life! reset sequence! so on!
                buttList = getButtons();
                for(final Button curButt : buttList) {
                    ValueAnimator animation = ValueAnimator.ofObject(new ArgbEvaluator(), Color.GRAY, Color.RED);
                    animation.setDuration(500);
                    animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            curButt.setBackgroundColor((int) animator.getAnimatedValue());
                        }

                    });
                    animation.start();
                }
            }



        }
    };

    private List<Button> getButtons() {
        List<Button> buttList = new ArrayList<>();

        for(int i = 0; i < buttIdList.size(); i++) {
            MyAdapter.ButtonViewHolder holder = (MyAdapter.ButtonViewHolder) mRV.findViewHolderForAdapterPosition(i);
            buttList.add(holder.gridButt);
        }

        return buttList;
    }
 }
