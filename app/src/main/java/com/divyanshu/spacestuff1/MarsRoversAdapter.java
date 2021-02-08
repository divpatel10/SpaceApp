package com.divyanshu.spacestuff1;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MarsRoversAdapter extends RecyclerView.Adapter<MarsRoversAdapter.MarsRoversViewHolder> {

    private final Context mContext;
    private final ArrayList<RoverModel>  roverModelArrayList;


    public MarsRoversAdapter(Context context, ArrayList<RoverModel> roverModels){
        mContext = context;
        roverModelArrayList = roverModels;

    }

    @NonNull
    @Override
    public MarsRoversViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.rover_list_view_items,parent,false);
            return new MarsRoversViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MarsRoversViewHolder holder, int position) {
            RoverModel currentRoverModel = roverModelArrayList.get(position);
            String imageURL = currentRoverModel.getImageView();

              //Higher android devices don't allow http connections!
                imageURL.replace("http:/","https:/");
                 Log.d("Link",imageURL);

        Picasso.get().load(imageURL).placeholder(R.drawable.icon_update).centerInside().fit().into(holder.imageView);



            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext.getApplicationContext(), FullScreenImage.class);
                    intent.putExtra("image", imageURL);
                    mContext.startActivity(intent);
                }
            });

            holder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {

                        Uri uri = Uri.parse(imageURL);
                        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
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
                        Toast.makeText(mContext.getApplicationContext(), "Error Downloading", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    @Override
    public int getItemCount() {
        return roverModelArrayList.size();
    }


    public class MarsRoversViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public ImageButton imageButton;
        public MarsRoversViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.mars_rover_image);
            imageButton = itemView.findViewById(R.id.rover_image_download);
        }


    }
}
