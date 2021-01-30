package com.divyanshu.spacestuff1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class ApodActivity extends AppCompatActivity {

    private TextView mTitleText;
    private TextView mAuthorText;
    private PhotoView mImageView;
    private String imageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apod);

        mTitleText = (TextView)findViewById(R.id.titleText);
        mAuthorText = (TextView)findViewById(R.id.authorText);
        mImageView = findViewById(R.id.imageView);

    }

    public void searchApoc(View view) {

        mAuthorText.setText("");
        mTitleText.setText(R.string.loading);

        new FetchNasaInfo(mTitleText, mAuthorText,mImageView).execute();


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
            return NetworkUtils.getNasaApod();

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
                //...

                JSONArray itemsArray = new JSONArray(s);
                JSONObject indexJSON = itemsArray.getJSONObject(0);
                picTopic = indexJSON.getString("title");
                picExplanation = indexJSON.getString("explanation");
                imageURL = indexJSON.getString("hdurl");

                if (picTopic != null && picExplanation != null) {
                    mTitleText.get().setText(picTopic);
                    mAuthorText.get().setText(picExplanation);

                }
                else {
                    mTitleText.get().setText("Something went wrong mate");
                    mAuthorText.get().setText("");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            Picasso.get().load(imageURL).into(mImageView.get());
        }
    }

}