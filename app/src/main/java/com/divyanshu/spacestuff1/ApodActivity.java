package com.divyanshu.spacestuff1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ApodActivity extends AppCompatActivity {

    private TextView mTitleText;
    private TextView mAuthorText;
    private PhotoView mImageView;
    private String imageURL_hd="";
    private String imageURL ="";
    private boolean isDateSearch = false;
    private String apod_date = "";
    private static Calendar calendar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apod);
        getSupportActionBar().hide();


        mTitleText = (TextView) findViewById(R.id.titleText);
        mAuthorText = (TextView) findViewById(R.id.authorText);
        mImageView = findViewById(R.id.imageView);
        calendar1 = Calendar.getInstance();

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ApodActivity.this, FullScreenImage.class);
                intent.putExtra("image", imageURL_hd);
                startActivity(intent);
            }
        });
    }

    //Function that is called when button named "random" is pressed.
    //This function calls the Async task FetchNasaInfo, and sets boolean value isDateSearch to false
    public void searchApoc(View view) {
        isDateSearch = false;
        mAuthorText.setText("");
        mTitleText.setText(R.string.loading);
        apod_date = null;
        new FetchNasaInfo(mTitleText, mAuthorText, mImageView).execute();

    }

    public void searchDate(View view) {
        isDateSearch = true;
        mAuthorText.setText("");
        mTitleText.setText(R.string.loading);

        new DatePickerDialog(ApodActivity.this,
                mDateSetListener
                , calendar1.get(Calendar.YEAR)
                , calendar1.get(Calendar.MONTH)
                , calendar1.get(Calendar.DAY_OF_MONTH)).show();


    }

    //Function that downloads the image taking url from String variable imageURL_hd
    public void downloadImage(View view) {
        try {

        Uri uri = Uri.parse(imageURL_hd);
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        String fileName = imageURL_hd.substring(imageURL_hd.lastIndexOf('/') + 1);
        request.setTitle(fileName);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"/images");
        request.setMimeType("*/*");
        downloadManager.enqueue(request);
        } catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error Downloading", Toast.LENGTH_SHORT).show();
        }

    }



    public class FetchNasaInfo extends AsyncTask<String, Void, String> {

        //Weak References used to prevent memory leaks
        private final WeakReference<TextView> mTitleText;
        private final WeakReference<TextView> mAuthorText;
        private final WeakReference<PhotoView> mImageView;

        //Constructor to initialize weakpointers with their corresponding views
        FetchNasaInfo(TextView titleText, TextView authorText, PhotoView imageView) {
            this.mTitleText = new WeakReference<>(titleText);
            this.mAuthorText = new WeakReference<>(authorText);
            this.mImageView = new WeakReference<>(imageView);

        }

        @Override
        protected String doInBackground(String... strings) {
            //Returns the query searched for JSON

            return NetworkUtils.getNasaApod(apod_date);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String picTopic;
            String picExplanation;
            if(s==null){
                Toast.makeText(getApplicationContext(),"Oops, something went wrong! Try again!",Toast.LENGTH_SHORT);
                return;
            }
            try {

                //if the image search query came with a date, parse json according to it
                if(!isDateSearch){
                    JSONArray itemsArray = new JSONArray(s);
                    JSONObject indexJSON = itemsArray.getJSONObject(0);
                    picTopic = indexJSON.getString("title");
                    picExplanation = indexJSON.getString("explanation");
                    imageURL_hd = indexJSON.getString("hdurl");
                    imageURL = indexJSON.getString("url");
                }
                else{
                        JSONObject indexJSON = new JSONObject(s);
                        picTopic = indexJSON.getString("title");
                        picExplanation = indexJSON.getString("explanation");
                        imageURL_hd = indexJSON.getString("hdurl");
                        imageURL = indexJSON.getString("url");

                }

                if (picTopic != null && picExplanation != null) {
                    mTitleText.get().setText(picTopic);
                    mAuthorText.get().setText(picExplanation);

                }
                else {
                    mTitleText.get().setText(R.string.error_message);
                    mAuthorText.get().setText("");
                }


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(ApodActivity.this,"Error retrieving information",Toast.LENGTH_SHORT).show();
            }

        // If the standard image does not exist, load the HD image
            if(imageURL == null || imageURL.equals("")) {
                Picasso.get().load(imageURL_hd).into(mImageView.get());

            }
            else{

            Picasso.get().load(imageURL).into(mImageView.get());
            }
        }
    }

    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @SuppressLint("SimpleDateFormat")
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, selectedDay);
            calendar.set(Calendar.MONTH, selectedMonth);
            calendar.set(Calendar.YEAR, selectedYear);

            calendar1.get(Calendar.DAY_OF_MONTH);
            calendar1.get(Calendar.MONTH);
            calendar1.get(Calendar.YEAR);


            if ((calendar1.getTimeInMillis() - calendar.getTimeInMillis()) / 86400000 >= 0){

            Date date = calendar.getTime();
            apod_date = new SimpleDateFormat("yyyy-MM-dd").format(date);
                new FetchNasaInfo(mTitleText, mAuthorText,mImageView).execute();
            }
            else {
                Toast.makeText(ApodActivity.this, "Please select a date before today", Toast.LENGTH_SHORT).show();
            }

        }
    };

}