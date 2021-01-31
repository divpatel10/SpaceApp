package com.divyanshu.spacestuff1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MainPageAdapter extends ArrayAdapter<String> {
    ArrayList<String> topics;
    ArrayList<Integer> descriptions;
    int[] backgroundRes = {R.drawable.forest_stars,R.drawable.rover_image,R.drawable.astronaut};
    int num_of_images = backgroundRes.length;

    TextView tv;
    public MainPageAdapter(@NonNull Context context, ArrayList<String> arrayList) {
        super(context, 0,arrayList);
        topics = arrayList;
        descriptions = new ArrayList<Integer>();
                    descriptions.add(R.string.apod_description);
                    descriptions.add(R.string.rover_description);
                    descriptions.add(R.string.iss_description);

    } @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listViewAdapter = convertView;

        if (listViewAdapter == null) {
            listViewAdapter = LayoutInflater.from(getContext()).inflate(R.layout.main_list, parent, false);
        }

        tv =(TextView)  listViewAdapter.findViewById(R.id.main_list_text);
        tv.setText(topics.get(position));
        ImageView imageView = (ImageView) listViewAdapter.findViewById(R.id.main_list_image);
        imageView.setImageResource(backgroundRes[position% num_of_images]);
        TextView textView = listViewAdapter.findViewById(R.id.main_list_description);
        textView.setText(descriptions.get(position));

        return listViewAdapter;
    }
}

