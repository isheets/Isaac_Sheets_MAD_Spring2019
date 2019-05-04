package com.example.afinal;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.support.design.widget.FloatingActionButton;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    public static RestAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        //create an adapter
        listAdapter = new RestAdapter(Restaurant.restList);

        //divider line between rows
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        //assign the adapter to the recycle view
        recyclerView.setAdapter(listAdapter);

        //set a layout manager on the recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //load data from shared prefs if its there
        if(Restaurant.restList.isEmpty()) {
            Restaurant.loadData(this);
        }
        //only load json if the list is still empty (likely a first time use)
        if(Restaurant.restList.isEmpty()) {
            new RestAdapter.FetchList().execute();
        }




        //fab
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //create alert dialog to add new restaurant
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                Context context = MainActivity.this;
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);


                final EditText titleBox = new EditText(context);
                titleBox.setHint("Title");
                layout.addView(titleBox); // Notice this is an add method

                final EditText urlBox = new EditText(context);
                urlBox.setHint("Url");
                layout.addView(urlBox);

                dialog.setView(layout);
                //set dialog title
                dialog.setTitle("Add Restaurant");
                //sets OK action
                dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //get entered details
                        String newTitle = titleBox.getText().toString();
                        String newURL = urlBox.getText().toString();
                        if (!newTitle.isEmpty()) {
                            // add restaurant
                            listAdapter.addRestaurant(newTitle, newURL);
                        }
                        Snackbar.make(view, "Restaurant added", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
                //sets cancel action
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // cancel
                    }
                });
                //present alert dialog
                dialog.show();
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        //save data
        Restaurant.storeData(this);
    }
}
