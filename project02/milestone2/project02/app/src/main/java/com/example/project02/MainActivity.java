package com.example.project02;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {
    RecyclerView mRV;

    List<Integer> buttIdList = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRV = findViewById(R.id.recycler);

        GridLayoutManager mGM = new GridLayoutManager(MainActivity.this, 4, GridLayoutManager.VERTICAL, false);
        mRV.setLayoutManager(mGM);

        for(int i = 0; i < 20; i++) {
            buttIdList.add(i);
        }

        MyAdapter myAdapter = new MyAdapter(MainActivity.this, buttIdList);
        mRV.setAdapter(myAdapter);

        myAdapter.setOnItemClickListener(onItemClickListener);
    }

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int clickTag = viewHolder.getAdapterPosition();

            Log.d("debuglog", "Clicked:" + clickTag);
            List<Button> buttList = getButtons();
            for(final Button curButt : buttList) {
                ValueAnimator animation = ValueAnimator.ofObject(new ArgbEvaluator(),Color.DKGRAY, Color.GREEN);
                animation.setDuration(1000);
                animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        curButt.setBackgroundColor((int) animator.getAnimatedValue());
                    }

                });
                animation.start();
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
