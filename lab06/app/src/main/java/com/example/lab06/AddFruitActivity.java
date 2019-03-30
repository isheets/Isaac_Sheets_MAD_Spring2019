package com.example.lab06;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class AddFruitActivity extends AppCompatActivity {
    TextView name;
    TextView season;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fruit);

        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("New Fruit");

        name = findViewById(R.id.nameInput);
        season = findViewById(R.id.seasonInput);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //get the ID of the item on the action bar that was clicked
        switch (item.getItemId()){
            case R.id.save:

                //TODO: get instances of textviews, check null, create new fruit
                String newName = name.getText().toString();
                String newSeason = season.getText().toString();
                if (!newName.isEmpty() && !newSeason.isEmpty()) {
                    Fruit.fruits.add(new Fruit(newName, newSeason));
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(this, "All fields required!",
                            Toast.LENGTH_LONG).show();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
