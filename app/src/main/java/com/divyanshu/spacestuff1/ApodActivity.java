package com.divyanshu.spacestuff1;

import androidx.appcompat.app.AppCompatActivity;

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
    private String imageURL;
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
                intent.putExtra("image", imageURL);
                startActivity(intent);
            }
        });
    }

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

        new DatePickerDialog(ApodActivity.this, mDateSetListener, calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH)).show();


    }

    public void downloadImage(View view) {
        try {

        Uri uri = Uri.parse(imageURL);
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        String fileName = imageURL.substring(imageURL.lastIndexOf('/') + 1);
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

        private WeakReference<TextView> mTitleText;
        private WeakReference<TextView> mAuthorText;
        private WeakReference<PhotoView> mImageView;


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

                if(!isDateSearch){
                    JSONArray itemsArray = new JSONArray(s);
                    JSONObject indexJSON = itemsArray.getJSONObject(0);
                    picTopic = indexJSON.getString("title");
                    picExplanation = indexJSON.getString("explanation");
                    imageURL = indexJSON.getString("hdurl");

                }
                else{
                        JSONObject indexJSON = new JSONObject(s);
                        picTopic = indexJSON.getString("title");
                        picExplanation = indexJSON.getString("explanation");
                        imageURL = indexJSON.getString("hdurl");

                }

                if (picTopic != null && picExplanation != null) {
                    mTitleText.get().setText(picTopic);
                    mAuthorText.get().setText(picExplanation);

                }
                else {
                    mTitleText.get().setText("Something went wrong");
                    mAuthorText.get().setText("");
                }


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(ApodActivity.this,"Error retrieving information",Toast.LENGTH_SHORT).show();
            }

            Picasso.get().load(imageURL).into(mImageView.get());
        }
    }

    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            int startYear = selectedYear;
            int startMonth = selectedMonth;
            int startDay = selectedDay;

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, startDay);
            calendar.set(Calendar.MONTH, startMonth);
            calendar.set(Calendar.YEAR, startYear);

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