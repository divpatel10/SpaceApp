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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.mainList);

        ArrayList<String> arrayList = new ArrayList<>();
        Collections.addAll(arrayList,titles);
        ArrayAdapter arrayAdapter =  new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
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
                }
            }
        });

    }


}