package com.divyanshu.spacestuff1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    String[] titles = {"Nasa APOC","Mars Rovers","Live ISS tracker"};
    ArrayList<Integer> descriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        ListView listView = findViewById(R.id.mainList);

        ArrayList<String> arrayList = new ArrayList<>();
        Collections.addAll(arrayList,titles);

        descriptions = new ArrayList<Integer>();
        descriptions.add(R.string.apod_description);
        descriptions.add(R.string.rover_description);
        descriptions.add(R.string.iss_description);

        ArrayAdapter arrayAdapter = new MainPageAdapter(MainActivity.this,arrayList, descriptions);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int positon, long id) {

                if(positon==0){
                    Intent intent = new Intent(getApplicationContext(), ApodActivity.class);
                        startActivity(intent);
                }
                else if(positon==1){
                    Intent intent = new Intent(getApplicationContext(), MarsRoversActivity.class);
                        startActivity(intent);
                }  else if(positon==2){
                    Intent intent = new Intent(getApplicationContext(), ISSTracker.class);
                        startActivity(intent);
                }
            }
        });

    }


}