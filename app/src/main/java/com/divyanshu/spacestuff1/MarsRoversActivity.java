package com.divyanshu.spacestuff1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MarsRoversActivity extends AppCompatActivity {
    private List<String> imageURL;
    private ListView mListView;
    private MarsRoversAdapter marsRoversAdapter;
    private ArrayList<RoverModel> modelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mars_rovers);
        imageURL = null;
        mListView = findViewById(R.id.recycler_view);
        modelArrayList = new ArrayList<>();


        new FetchRoverInfo().execute();

    }


    public class FetchRoverInfo extends AsyncTask<String, Void, String> {


        void FetchRoverInfo() {

        }

        @Override
        protected String doInBackground(String... strings) {
            //Returns the query searched for JSON

            return NetworkUtils.getMarsRoverImages();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

                String imgstring;

            try {
                //...
                JSONObject jsonObject = new JSONObject(s);
                Log.d("BRUH",jsonObject.toString());

                JSONArray itemsArray = jsonObject.getJSONArray("photos");
                Log.d("BRUH",itemsArray.toString());
                for(int i=0; i<itemsArray.length();i++){
                    JSONObject indexJSON = itemsArray.getJSONObject(i);
                    imgstring = indexJSON.getString("img_src");
                    modelArrayList.add(new RoverModel(imgstring));
                }

                mListView.setAdapter(new MarsRoversAdapter(MarsRoversActivity.this, modelArrayList));


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

}