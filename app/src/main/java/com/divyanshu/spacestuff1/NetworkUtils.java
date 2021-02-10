package com.divyanshu.spacestuff1;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {
    private static final String LOG_TAG =  NetworkUtils.class.getSimpleName();

    private static final String BASE_URL = "https://api.nasa.gov/planetary/apod?";
    private static final String API_KEY = "api_key";
    private static final String RANDOM_ONE = "count";
    private static final String key = "KpPKYvbEQzkIwY4QwQBnMpU1Srnwp8Uel2gl976B";
    private static final String ROVER_URL = "https://api.nasa.gov/mars-photos/api/v1/rovers/";
    private static final String SOL_DAYS = "sol";
    private static final String PHOTOS = "/photos?";


    static String getNasaApod(String apod_date){


        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String APODimageString = null;

        try {
            Uri builtURI;
            if(apod_date == null) {


                builtURI  = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY, key)
                        .appendQueryParameter(RANDOM_ONE, "1")
                        .build();
            }
            else{
                 builtURI = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY, key)
                        .appendQueryParameter("date", apod_date)
                        .build();
            }

            URL requestURL = new URL(builtURI.toString());

            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Get the InputStream.
            InputStream inputStream = urlConnection.getInputStream();

            // Create a buffered reader from that input stream.
            reader = new BufferedReader(new InputStreamReader(inputStream));

            // Use a StringBuilder to hold the incoming response.
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);

                builder.append("\n");
            }

            if (builder.length() == 0) {
                // dont parse if the stream is empty
                return null;
            }
            APODimageString = builder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return APODimageString;
    }


    static String getMarsRoverImages(String solDays, String rover){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String MarsRoverString = null;

            if(rover.equals("na")){
                rover = "Curiosity";
            }
            if(solDays.equals("na")){
                solDays = "50";

            }
        try {
            Uri builtURI;

                    builtURI = Uri.parse(ROVER_URL+rover+PHOTOS).buildUpon()
                    .appendQueryParameter(SOL_DAYS,solDays)
                    .appendQueryParameter(API_KEY, key)
                    .build();


            URL requestURL = new URL(builtURI.toString());
            Log.d("BRUH", requestURL.toString());

            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(100000);
            urlConnection.connect();

            // Get the InputStream.
            InputStream inputStream = urlConnection.getInputStream();

            // Create a buffered reader from that input stream.
            reader = new BufferedReader(new InputStreamReader(inputStream));

            // Use a StringBuilder to hold the incoming response.
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);

                builder.append("\n");
            }

            if (builder.length() == 0) {
                // dont parse if the stream is empty
                return null;
            }
            MarsRoverString = builder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return MarsRoverString;

    }

    static String getISSLocation(){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String ISSLocation = null;

        try {
            //...
            Uri builtURI = Uri.parse("https://api.wheretheiss.at/v1/satellites/25544").buildUpon().build();


            URL requestURL = new URL(builtURI.toString());

            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Get the InputStream.
            InputStream inputStream = urlConnection.getInputStream();

            // Create a buffered reader from that input stream.
            reader = new BufferedReader(new InputStreamReader(inputStream));

            // Use a StringBuilder to hold the incoming response.
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);

                builder.append("\n");
            }

            if (builder.length() == 0) {
                // dont parse if the stream is empty
                return null;
            }
            ISSLocation = builder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return ISSLocation;

    }




}
