package com.hz_apps.timebasedlocker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hz_apps.timebasedlocker.Datebase.SavedPhoto;
import com.hz_apps.timebasedlocker.R;

import java.util.ArrayList;
import java.util.List;

public class SavedPhotosAdapter extends RecyclerView.Adapter<SavedPhotosAdapter.myViewHolder> {
    private final Context context;
    private List<SavedPhoto> savedPhotoList;

    public SavedPhotosAdapter(Context context) {
        this.context = context;
        this.savedPhotoList = new ArrayList<>();
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.saved_photo_view, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        SavedPhoto savedPhoto = savedPhotoList.get(position);
        Glide.with(context).load("/data/data/" + context.getPackageName() + "/files/photos/" + savedPhoto.getId())
                .centerCrop()
                .override(10, 10)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return savedPhotoList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.saved_photo_image_view);
        }
    }
    public void setSavedPhotoList(List<SavedPhoto> savedPhotoList) {
        this.savedPhotoList = savedPhotoList;
    }
}
