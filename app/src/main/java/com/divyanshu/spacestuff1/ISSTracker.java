package com.divyanshu.spacestuff1;

import androidx.fragment.app.FragmentActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;


public class ISSTracker extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iss_tracker);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                new getISSLoation().execute();
            }
        },0,1000);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        new getISSLoation().execute();
    }


    public class getISSLoation extends AsyncTask<String, Void, String> {



        @Override
        protected String doInBackground(String... strings) {
            //Returns the query searched for JSON
            return NetworkUtils.getISSLocation();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            double latitude;
            double longitude;

            try {

                JSONObject jsonObject = new JSONObject(s);

                latitude = jsonObject.getDouble("latitude");
                longitude = jsonObject.getDouble("longitude");

                if (longitude != 0 && longitude != 0) {

                    LatLng spaceStation = new LatLng(latitude,longitude);
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(spaceStation).title("ISS").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_iss)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(spaceStation));

                }
                else {
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}