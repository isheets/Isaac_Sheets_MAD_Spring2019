package com.example.afinal;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class RestAdapter extends RecyclerView.Adapter<RestAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView url;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nameTextView);
            url = itemView.findViewById(R.id.websiteTextView);
        }
    }

    private List<Restaurant> restList;

    public RestAdapter() {
    }

    public RestAdapter(List<Restaurant> list) {
        restList = list;
    }

    @NonNull
    @Override
    public RestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = layoutInflater.inflate(R.layout.list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RestAdapter.ViewHolder viewHolder, int i) {
        final Restaurant curRest = restList.get(i);
        viewHolder.name.setText(curRest.getTitle());
        viewHolder.url.setText(curRest.getUrl());


        //context menu
        viewHolder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener(){
            @Override
            public void onCreateContextMenu(ContextMenu menu, final View v, ContextMenu.ContextMenuInfo menuInfo) {
                //set the menu title
                menu.setHeaderTitle("Delete " + curRest.getTitle());
                //add the choices to the menu
                menu.add(1, 1, 1, "Yes").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        removeRestaurant(viewHolder.getAdapterPosition());
                        Snackbar.make(v, "Item removed", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        return false;
                    }
                });
                menu.add(2, 2, 2, "No");
            }
        });
    }

    @Override
    public int getItemCount() {
        return restList.size();
    }

    public void addRestaurant(String newTitle, String newURL){
        Log.d("debug", newTitle + " : " + newURL);
        Restaurant.restList.add(new Restaurant(newTitle, newURL));
        notifyItemInserted(getItemCount());
    }

    public void removeRestaurant(int itemPosition){
        Restaurant.restList.remove(itemPosition);
        notifyItemRemoved(itemPosition);
    }

    public static class FetchList extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... strings) {
            //make the request
            try {
                URL url = new URL("https://creative.colorado.edu/~apierce/samples/data/yelp_restaurants.json");
                //open connection
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                try {
                    //create an InputStreamReader with the JSON stream
                    InputStreamReader is = new InputStreamReader(urlConnection.getInputStream());
                    //convert the byte stream to a character stream using BufferedReader
                    BufferedReader bufferedReader = new BufferedReader(is);
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    //loop through the character stream and build a sequence of characters using StringBuilder
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    //convert character sequence to a String
                    return stringBuilder.toString();
                } finally {
                    //disconnect the http connection
                    urlConnection.disconnect();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        protected void onPostExecute(String response) {
            //process the response

            //check response
            if (response == null) {
                response = "ERROR";
            }

            Log.d("debug", response);

            try {
                JSONObject responseObject = (JSONObject) new JSONTokener(response).nextValue();
                //TODO: populate questions and options(random) (decode strings before displaying), keep track of correct (maybe look at swift logic)
                Log.d("debug", responseObject.toString(4));

                //get the question
                JSONArray title = responseObject.getJSONArray("restaurants");

                String curTitle;
                String curURL;

                for(int i = 0; i < title.length(); i++) {
                    curTitle = title.getJSONObject(i).getString("name");
                    curURL = title.getJSONObject(i).getString("url");
                    Log.d("debug", curTitle + " : " + curURL);
                    Restaurant curRest = new Restaurant(curTitle, curURL);
                    if(!Restaurant.restList.contains(curRest)) {
                        Restaurant.restList.add(curRest);
                    }
                }
                MainActivity.listAdapter.notifyDataSetChanged();


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
