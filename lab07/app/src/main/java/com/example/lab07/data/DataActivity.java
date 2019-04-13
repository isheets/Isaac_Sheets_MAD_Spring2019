package com.example.lab07.data;

import com.example.lab07.MyApp;
import android.content.res.XmlResourceParser;
import android.util.Log;

import com.example.lab07.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataActivity {
    public List<MovieContent.MovieItem> loadXML() throws XmlPullParserException, IOException {
        String new_title = new String();
        String new_url = new String();
        int id_counter = 0;
        List<MovieContent.MovieItem> movies=new ArrayList<MovieContent.MovieItem>();

        StringBuffer stringBuffer = new StringBuffer();

        //get xml file
        XmlResourceParser xpp = MyApp.getAppContext().getResources().getXml(R.xml.movies);

        //advances the parser to the next event
        xpp.next();
        //gets the event type/state of the parser
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    // start of document
                    break;
                case XmlPullParser.START_TAG:
                    if (xpp.getName().equals("movie")) {
                        stringBuffer.append("\nSTART_TAG: " + xpp.getName());
                    }
                    if (xpp.getName().equals("title")) {
                        stringBuffer.append("\nSTART_TAG: " + xpp.getName());
                        eventType = xpp.next();
                        new_title = xpp.getText(); //gets the title of the movie
                    }
                    else if (xpp.getName().equals("url")) {
                        stringBuffer.append("\nSTART_TAG: " + xpp.getName());
                        eventType = xpp.next();
                        new_url = xpp.getText(); //gets the url of the movie
                        Log.i("data", "new url: " + new_url);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if (xpp.getName().equals("movie")) {
                        id_counter++;
                        //create new item object
                        MovieContent.MovieItem new_item = new MovieContent.MovieItem(String.valueOf(id_counter), new_title, new_url);
                        movies.add(new_item);
                    }
                    break;

                case XmlPullParser.TEXT:
                    break;
            }
            eventType = xpp.next();
        }

        return movies;

    }
}
