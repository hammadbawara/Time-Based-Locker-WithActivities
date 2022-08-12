package com.hz_apps.timebasedlocker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hz_apps.timebasedlocker.Datebase.SavedVideo;
import com.hz_apps.timebasedlocker.R;

import java.util.ArrayList;
import java.util.List;

public class SavedVideosAdapter extends RecyclerView.Adapter<SavedVideosAdapter.myViewHolder> {

    private final Context context;
    private List<SavedVideo> savedVideoList;

    public SavedVideosAdapter(Context context) {
        this.context = context;
        this.savedVideoList = new ArrayList<>();
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.saved_photo_view, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        SavedVideo savedVideo = savedVideoList.get(position);
        Glide.with(context).load("/data/data/" + context.getPackageName() + "/files/videos/" + savedVideo.getId())
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return savedVideoList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.saved_photo_image_view);
        }
    }

    public void setSavedVideoList(List<SavedVideo> savedVideoList) {
        this.savedVideoList = savedVideoList;
    }
}
