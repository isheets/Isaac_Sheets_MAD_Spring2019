package com.example.lab08;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        //create an adapter
        listAdapter = new ListAdapter(Todo.todoList);

        //divider line between rows
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        //assign the adapter to the recycle view
        recyclerView.setAdapter(listAdapter);

        //set a layout manager on the recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //load data
        if(Todo.todoList.isEmpty()) {
            Todo.loadData(this);
        }

        //fab
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //create alert dialog to add new todo
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                //create edit text
                final EditText edittext = new EditText(getApplicationContext());
                //add edit text to dialog
                dialog.setView(edittext);
                //set dialog title
                dialog.setTitle("Add Todo");
                //sets OK action
                dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //get todo entered
                        String newTitle = edittext.getText().toString();
                        if (!newTitle.isEmpty()) {
                            // add todo
                            listAdapter.addTodo(newTitle);
                        }
                        Snackbar.make(view, "Todo added", Snackbar.LENGTH_LONG)
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
        Todo.storeData(this);
    }
}
