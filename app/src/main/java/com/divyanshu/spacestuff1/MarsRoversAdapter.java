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
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MarsRoversAdapter extends ArrayAdapter<RoverModel> {
        Context mContext;

    public MarsRoversAdapter(@NonNull Context context,ArrayList<RoverModel> arrayList ) {
        super(context,0,arrayList);
        mContext = context;

    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listViewItem = convertView;
        RoverModel roverModel = getItem(position);
        if(listViewItem==null){
            listViewItem = LayoutInflater.from(getContext()).inflate(R.layout.rover_list_view_items, parent, false);

        }
            Picasso.get().load(roverModel.getImageView()).into((ImageView) listViewItem.findViewById(R.id.mars_rover_image));
        return listViewItem;
    }

}
