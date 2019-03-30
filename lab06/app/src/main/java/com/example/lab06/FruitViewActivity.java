package com.example.lab06;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class FruitViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit_view);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get fruit id and instance
        int fruitNum = (Integer)getIntent().getExtras().get("fruitId");
        Fruit fruit = Fruit.fruits.get(fruitNum);

        //set textviews
        TextView fruitName = findViewById(R.id.fruitNameTextView);
        fruitName.setText(fruit.getName());

        TextView fruitSeason = findViewById(R.id.seasonTextView);
        fruitSeason.setText("Best season: " + fruit.getSeason());

    }
}
