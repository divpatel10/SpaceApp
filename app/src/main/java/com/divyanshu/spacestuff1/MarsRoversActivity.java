package com.divyanshu.spacestuff1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    private RecyclerView mRecyclerView;
    private MarsRoversAdapter marsRoversAdapter;
    private ArrayList<RoverModel> modelArrayList;
    private RadioButton radioButton;
    private String roverSelected="na";
    private String solSelected="na";
    TextView textView;
    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mars_rovers);
        mRecyclerView = findViewById(R.id.recycler_view);
        getSupportActionBar().hide();

        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);
        modelArrayList = new ArrayList<>();
        marsRoversAdapter = new MarsRoversAdapter(MarsRoversActivity.this,modelArrayList);
        mRecyclerView.setAdapter(marsRoversAdapter);
         textView = findViewById(R.id.roverName);
        textView.setText("Images taken by Curiosity");
        textView2 = findViewById(R.id.sol_info);
        textView2.setText("SOL: 50");

        new FetchRoverInfo().execute();



    }

    public void chooseRover(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MarsRoversActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.choose_rover,null);
        RadioGroup group = dialogView.findViewById(R.id.radioGroupRovers);

        builder.setTitle("Choose Rover")
                .setView(dialogView)
                .setCancelable(false)
                .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int selectedRover = group.getCheckedRadioButtonId();
                        radioButton = dialogView.findViewById(selectedRover);
                        roverSelected = (String) radioButton.getText();
                        textView.setText(String.format("Picture taken by %s", roverSelected));
                        new FetchRoverInfo().execute();

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

    }

    public void chooseSol(View view) {
        android.app.AlertDialog.Builder alertBox;

        EditText editText = new EditText(MarsRoversActivity.this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        alertBox = new android.app.AlertDialog.Builder(MarsRoversActivity.this);
        alertBox.setMessage("A Sol is one solar day on Mars");
        alertBox.setTitle("Enter Sol");
        alertBox.setView(editText);
        alertBox.setPositiveButton("Enter", (dialog, which) -> {
            if(editText.getText() == null || editText.getText().toString().length() ==0){
                Toast.makeText(MarsRoversActivity.this.getApplicationContext(), "Please enter a number", Toast.LENGTH_SHORT).show();
                return;
            }

            alertBox.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            solSelected = editText.getText().toString();
            textView2.setText(String.format("SOL: %s", solSelected));
            new FetchRoverInfo().execute();

        });
        alertBox.create().show();

    }


    public class FetchRoverInfo extends AsyncTask<String, Void, String> {


         FetchRoverInfo() {
        }

        @Override
        protected String doInBackground(String... strings) {
            //Returns the query searched for JSON
            return NetworkUtils.getMarsRoverImages(solSelected,roverSelected);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mRecyclerView.removeAllViewsInLayout();
            modelArrayList.clear();

            String imgstring="";

            try {
                JSONObject jsonObject = new JSONObject(s);

                JSONArray itemsArray = jsonObject.getJSONArray("photos");
                for(int i=0; i<itemsArray.length();i++){
                    JSONObject indexJSON = itemsArray.getJSONObject(i);
                    imgstring = indexJSON.getString("img_src");
                    Log.d("BRUH",imgstring);

                    modelArrayList.add(new RoverModel(imgstring));
                }


                marsRoversAdapter = new MarsRoversAdapter(MarsRoversActivity.this,modelArrayList);
                mRecyclerView.setAdapter(marsRoversAdapter);

                if (imgstring.equals("")) {
                    Toast.makeText(getApplicationContext(),"Something went wrong!",Toast.LENGTH_LONG).show();

                }


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Error Retrieving Data!",Toast.LENGTH_LONG).show();

            }


        }
    }

}