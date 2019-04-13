package com.example.lab07.data;

import android.graphics.Movie;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieContent {

    public static final List<MovieItem> ITEMS = new ArrayList<MovieItem>();

    public static final Map<String, MovieItem> ITEM_MAP = new HashMap<String, MovieItem>();

    public void getData() {
        Log.i("data", "getData() called");
        List<MovieContent.MovieItem> xmlData = new ArrayList<MovieContent.MovieItem>();
        DataActivity xmlDataActivity = new DataActivity();
        if (ITEMS.size() == 0) {
            try {
                xmlData = xmlDataActivity.loadXML();
                Log.i("data", "XML data");
                for(int i = 0; i <xmlData.size(); i++) {
                    addItem(xmlData.get(i));
                }

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static void addItem(MovieItem item) {
        Log.i("data", item.title);
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static class MovieItem {
        public final String id;
        public final String title;
        public final String url;

        public MovieItem(String id, String title, String url) {
            this.id = id;
            this.title = title;
            this.url = url;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
