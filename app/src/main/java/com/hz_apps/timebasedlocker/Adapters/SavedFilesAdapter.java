package com.hz_apps.timebasedlocker.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hz_apps.timebasedlocker.Datebase.SavedFile;
import com.hz_apps.timebasedlocker.R;
import com.hz_apps.timebasedlocker.ui.LockFiles.DateAndTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class SavedFilesAdapter extends RecyclerView.Adapter<SavedFilesAdapter.myViewHolder> {
    private final Context context;
    private List<SavedFile> savedPhotoList;

    public SavedFilesAdapter(Context context, List<SavedFile> savedFileList) {
        this.context = context;
        this.savedPhotoList = savedFileList;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.saved_file_view, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        SavedFile file  = savedPhotoList.get(position);

        Glide.with(context).load(file.getPath())
                .centerCrop()
                .placeholder(R.drawable.ic_image)
                .into(holder.imageView);

        holder.time_remaining.setText(getRemainingTime(file.getLockDateTime(), file.getUnlockDateTime()));

        // Setting click listener on saved file
        holder.itemView.setOnLongClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("file", file);
            Navigation.findNavController(v).navigate(R.id.BSDialog, bundle);
            return false;
        });



    }

    @Override
    public int getItemCount() {
        return savedPhotoList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        private final TextView time_remaining;
        private final ImageView imageView;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.saved_photo_image_view);
            time_remaining = itemView.findViewById(R.id.time_remaing_textView);
        }
    }

    private String getRemainingTime(DateAndTime lockDT, DateAndTime unlockDT){
        LocalDate lDate = lockDT.getDate();
        LocalTime lTime = lockDT.getTime();
        LocalDate unlDate = unlockDT.getDate();
        LocalTime unTime = unlockDT.getTime();

        if ((unlDate.getYear() - lDate.getYear())>0){
            return ((unlDate.getYear() - lDate.getYear() + " years"));
        }
        if ((unlDate.getMonthValue() - lDate.getMonthValue())>0){
            return ((unlDate.getMonthValue() - lDate.getMonthValue() + " mon"));
        }
        if ((unlDate.getDayOfMonth() - lDate.getDayOfMonth())>0){
            return (unlDate.getDayOfMonth() - lDate.getDayOfMonth()) + " days";
        }
        if ((unTime.getHour() - lTime.getHour())>0){
            return (unTime.getHour() - lTime.getHour()) + " hours";
        }
        if ((unTime.getMinute() - lTime.getMinute())>0){
            return (unTime.getMinute() - lTime.getMinute()) + " min";
        }
        if ((unTime.getSecond() - lTime.getSecond())>0){
            return (unTime.getMinute() - lTime.getMinute()) + " sec";
        }

        return "unlocked";

    }
}
