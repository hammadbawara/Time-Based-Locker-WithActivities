package com.hz_apps.timebasedlocker.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.hz_apps.timebasedlocker.Datebase.SavedFile;
import com.hz_apps.timebasedlocker.R;
import com.hz_apps.timebasedlocker.ui.LockFiles.DateAndTime;
import com.hz_apps.timebasedlocker.ui.videos.VideoPlayerActivity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class SavedFilesAdapter extends RecyclerView.Adapter<SavedFilesAdapter.myViewHolder> {
    private final Context context;
    private final List<SavedFile> savedPhotoList;
    private ActionMode mActionMode;
    private final MaterialToolbar toolbar;
    private boolean[] selectedFilesList;
    private int numberOfSelectedFiles = 0;

    public SavedFilesAdapter(Context context, List<SavedFile> savedFileList, MaterialToolbar toolbar) {
        this.context = context;
        this.savedPhotoList = savedFileList;
        this.toolbar = toolbar;
        selectedFilesList = new boolean[savedFileList.size()];
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.saved_file_view, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        // Select file if it is selected if it is not selected then unselect
        if (selectedFilesList[holder.getBindingAdapterPosition()]){
            selectItem(holder);
        }else {
            unselectItem(holder);
        }

        SavedFile file  = savedPhotoList.get(position);

        Glide.with(context).load(file.getPath())
                .centerCrop()
                .placeholder(R.drawable.ic_image)
                .into(holder.imageView);

        holder.time_remaining.setText(getRemainingTime(file.getLockDateTime(), file.getUnlockDateTime()));


        holder.itemView.setOnClickListener(v -> {
            if (mActionMode != null){
                if (selectedFilesList[position]){
                    unselectItem(holder);
                }else {
                    selectItem(holder);
                }
            }else{
                Intent intent = new Intent(context, VideoPlayerActivity.class);
                intent.putExtra("video_path", file.getPath());
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (mActionMode != null){
                return false;
            }
            selectItem(holder);
            mActionMode = toolbar.startActionMode(mActionModeCallBack);
            return true;
        });



    }

    @Override
    public int getItemCount() {
        return savedPhotoList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        private final TextView time_remaining;
        private final ImageView imageView;
        private final ImageView checkbox;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.saved_photo_image_view);
            time_remaining = itemView.findViewById(R.id.time_remaing_textView);
            checkbox = itemView.findViewById(R.id.check_box_saved_file);
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

    private final ActionMode.Callback mActionModeCallBack = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.action_mode_saved_file, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()){
                case R.id.select_all_saved_file:
                    selectAllItems();
                    break;
                case R.id.unselect_all_saved_file:
                    unselectAllItems();
                    break;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            unselectAllItems();
            mActionMode = null;
        }
    };

    private void selectItem(myViewHolder holder) {
        holder.checkbox.setVisibility(View.VISIBLE);
        holder.imageView.setColorFilter(ContextCompat.getColor(context, R.color.black_opacity_25),
                PorterDuff.Mode.SRC_OVER);
        selectedFilesList[holder.getBindingAdapterPosition()] = true;
        numberOfSelectedFiles += 1;
    }

    private void unselectItem(myViewHolder holder){
        holder.checkbox.setVisibility(View.GONE);
        holder.imageView.clearColorFilter();
        selectedFilesList[holder.getBindingAdapterPosition()] = false;
        numberOfSelectedFiles -= 1;
    }

    public void unselectAllItems(){
        for (int i=0; i<selectedFilesList.length; i++){
            if (selectedFilesList[i]){
                selectedFilesList[i] = false;
                notifyItemChanged(i);
            }
        }
    }

    public void selectAllItems(){
        for (int i=0; i<selectedFilesList.length; i++){
            if (!selectedFilesList[i]){
                selectedFilesList[i] = true;
                notifyItemChanged(i);
            }
        }
    }
}
