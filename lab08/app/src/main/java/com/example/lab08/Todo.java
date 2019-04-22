package com.example.lab08;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Todo {
    private String title;
    private Boolean checked;

    private static final String PREFS_NAME = "TODOS";

    public Todo(String newTitle, Boolean newCheck) {
        title = newTitle;
        checked = newCheck;
    }

    public static List<Todo> todoList = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean newCheck) {
        checked = newCheck;
    }


    public static void storeData(Context context) {
        //get access to a shared preferences file
        SharedPreferences sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        //create an editor to write to the shared preferences file
        SharedPreferences.Editor editor = sharedPrefs.edit();
        //add size to the editor
        editor.putInt("size", todoList.size());
        //add items to the editor
        for (int i = 0; i < todoList.size(); i++) {
            editor.putString("title" + i, todoList.get(i).getTitle());
            editor.putBoolean("checked" + i, todoList.get(i).getChecked());
        }

        editor.apply();
    }

    public static void loadData(Context context) {
        //get access to a shared preferences file
        SharedPreferences sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        int size = sharedPrefs.getInt("size", 0);

        if(size>0) {
            for (int i = 0; i < size; i++) {
                todoList.add(new Todo(sharedPrefs.getString("title" + i, ""), sharedPrefs.getBoolean("checked" + i, false)));
            }
        }
    }

}
