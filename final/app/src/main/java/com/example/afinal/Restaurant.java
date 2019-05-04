package com.example.afinal;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    private String title;
    private String url;

    public static List<Restaurant> restList = new ArrayList<>();

    private static final String PREFS_NAME = "RESTAURANTS";

    public Restaurant(String newTitle, String newURL) {
        title = newTitle;
        url = newURL;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public static void storeData(Context context) {
        //get access to a shared preferences file
        SharedPreferences sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        //create an editor to write to the shared preferences file
        SharedPreferences.Editor editor = sharedPrefs.edit();
        //add size to the editor
        editor.putInt("size", restList.size());
        //add items to the editor
        for (int i = 0; i < restList.size(); i++) {
            editor.putString("title" + i, restList.get(i).getTitle());
            editor.putString("url" + i, restList.get(i).getUrl());
        }

        editor.apply();
    }

    public static void loadData(Context context) {
        //get access to a shared preferences file
        SharedPreferences sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        int size = sharedPrefs.getInt("size", 0);

        if(size>0) {
            for (int i = 0; i < size; i++) {
                restList.add(new Restaurant(sharedPrefs.getString("title" + i, ""), sharedPrefs.getString("url" + i, "")));
            }
        }
    }
}
