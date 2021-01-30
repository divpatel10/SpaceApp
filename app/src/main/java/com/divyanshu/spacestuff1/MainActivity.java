package com.divyanshu.spacestuff1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    String[] titles = {"Nasa APOC","Mars Rovers","Live ISS tracker"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String jsonURL="abc";
        String picTopic;
        String picExplanation;
        String imageURL;



        //This stuff should go in AsyncTask since it may take time to load
        JSONArray itemsArray = null;

        try {
            itemsArray = new JSONArray(jsonURL);
            JSONObject indexJSON = itemsArray.getJSONObject(0);
            picTopic = indexJSON.getString("title");
            picExplanation = indexJSON.getString("explanation");
            imageURL = indexJSON.getString("hdurl");
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}