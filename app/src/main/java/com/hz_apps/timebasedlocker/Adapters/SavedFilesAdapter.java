package com.hz_apps.timebasedlocker.Adapters;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hz_apps.timebasedlocker.Datebase.DBHelper;
import com.hz_apps.timebasedlocker.Datebase.SavedFile;
import com.hz_apps.timebasedlocker.R;
import com.hz_apps.timebasedlocker.databinding.CustomAlertDialogBinding;
import com.hz_apps.timebasedlocker.ui.LockFiles.DateAndTime;
import com.hz_apps.timebasedlocker.ui.ShowSavedFiles.SavedFilesActivity;
import com.hz_apps.timebasedlocker.ui.ViewMedia.VideoPlayerActivity;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class SavedFilesAdapter extends RecyclerView.Adapter<SavedFilesAdapter.myViewHolder> {
    private final Context context;
    private final List<SavedFile> savedFileList;
    private final MaterialToolbar toolbar;
    private ActionMode mActionMode;
    private boolean[] selectedFilesList;
    private int numberOfSelectedFiles = 0;
    private boolean isAllItemsSelected = false;
    private FloatingActionButton addSavedFiles;
    private final ActionMode.Callback mActionModeCallBack = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            addSavedFiles.setVisibility(View.INVISIBLE);
            mode.getMenuInflater().inflate(R.menu.action_mode_saved_file, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.select_all_saved_file:
                    if (isAllItemsSelected) {
                        unselectAllItems();
                        isAllItemsSelected = false;
                    } else {
                        selectAllItems();
                        isAllItemsSelected = true;
                    }
                    return true;
                case R.id.delete_btn_saved_file:
                    if (numberOfSelectedFiles > 0) {
                        askUserBeforeDeleting(mode);
                    }
                    return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            unselectAllItems();
            addSavedFiles.setVisibility(View.VISIBLE);
            mActionMode = null;
        }
    };

    public SavedFilesAdapter(Context context, List<SavedFile> savedFileList, MaterialToolbar toolbar, FloatingActionButton addSavedFiles) {
        this.context = context;
        this.savedFileList = savedFileList;
        this.toolbar = toolbar;
        this.addSavedFiles = addSavedFiles;
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
        if (selectedFilesList[holder.getBindingAdapterPosition()]) {
            selectItem(holder);
        } else {
            unselectItem(holder);
        }

        SavedFile file = savedFileList.get(position);

        switch (file.getFileType()){
            case DBHelper.PHOTO_TYPE:
                Glide.with(context)
                        .load(context.getDrawable(R.drawable.ic_image))
                        .into(holder.imageView);
                break;
            case DBHelper.VIDEO_TYPE:
                if (file.isAllowedToSeePhoto()){
                    Glide.with(context)
                            .load(file.getPath())
                            .centerCrop()
                            .into(holder.imageView);
                }else{
                    Glide.with(context)
                            .load(context.getDrawable(R.drawable.ic_video))
                            .into(holder.imageView);
                    break;
                }

        }


        holder.time_remaining.setText(getRemainingTime(file.getLockDateTime(), file.getUnlockDateTime()));


        holder.itemView.setOnClickListener(v -> {
            if (mActionMode != null) {
                if (selectedFilesList[position]) {
                    unselectItem(holder);
                    numberOfSelectedFiles -= 1;
                } else {
                    selectItem(holder);
                    numberOfSelectedFiles += 1;
                }
            } else {
                Intent intent = new Intent(context, VideoPlayerActivity.class);
                intent.putExtra("video_path", file.getPath());
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (mActionMode != null) {
                return false;
            }
            selectItem(holder);
            numberOfSelectedFiles += 1;
            mActionMode = toolbar.startActionMode(mActionModeCallBack);
            return true;
        });


    }

    @Override
    public int getItemCount() {
        return savedFileList.size();
    }

    private String getRemainingTime(DateAndTime lockDT, DateAndTime unlockDT) {
        LocalDate lDate = lockDT.getDate();
        LocalTime lTime = lockDT.getTime();
        LocalDate unlDate = unlockDT.getDate();
        LocalTime unTime = unlockDT.getTime();

        if ((unlDate.getYear() - lDate.getYear()) > 0) {
            return ((unlDate.getYear() - lDate.getYear() + " years"));
        }
        if ((unlDate.getMonthValue() - lDate.getMonthValue()) > 0) {
            return ((unlDate.getMonthValue() - lDate.getMonthValue() + " mon"));
        }
        if ((unlDate.getDayOfMonth() - lDate.getDayOfMonth()) > 0) {
            return (unlDate.getDayOfMonth() - lDate.getDayOfMonth()) + " days";
        }
        if ((unTime.getHour() - lTime.getHour()) > 0) {
            return (unTime.getHour() - lTime.getHour()) + " hours";
        }
        if ((unTime.getMinute() - lTime.getMinute()) > 0) {
            return (unTime.getMinute() - lTime.getMinute()) + " min";
        }
        if ((unTime.getSecond() - lTime.getSecond()) > 0) {
            return (unTime.getMinute() - lTime.getMinute()) + " sec";
        }

        return "unlocked";

    }

    private void selectItem(myViewHolder holder) {
        holder.checkbox.setVisibility(View.VISIBLE);
        holder.imageView.setColorFilter(ContextCompat.getColor(context, R.color.black_opacity_25),
                PorterDuff.Mode.SRC_OVER);
        selectedFilesList[holder.getBindingAdapterPosition()] = true;
    }

    private void unselectItem(myViewHolder holder) {
        holder.checkbox.setVisibility(View.GONE);
        holder.imageView.clearColorFilter();
        selectedFilesList[holder.getBindingAdapterPosition()] = false;
    }

    public void unselectAllItems() {
        for (int i = 0; i < selectedFilesList.length; i++) {
            if (selectedFilesList[i]) {
                selectedFilesList[i] = false;
                notifyItemChanged(i);
            }
        }
        numberOfSelectedFiles = 0;
    }

    public void selectAllItems() {
        for (int i = 0; i < selectedFilesList.length; i++) {
            if (!selectedFilesList[i]) {
                selectedFilesList[i] = true;
                notifyItemChanged(i);
            }
        }
        numberOfSelectedFiles = selectedFilesList.length;
    }

    private String createStringOfSelectedFiles() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < selectedFilesList.length; i++) {
            if (selectedFilesList[i]) {
                stringBuilder.append(savedFileList.get(i).getName()).append("\n");
            }
        }
        return stringBuilder.toString();
    }

    private void askUserBeforeDeleting(ActionMode mode) {
        CustomAlertDialogBinding binding = CustomAlertDialogBinding.inflate(LayoutInflater.from(context));
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setView(binding.getRoot());


        binding.fileNameTextView.setText(createStringOfSelectedFiles());

        dialog.setNegativeButton("Cancel", (dialog1, which) -> {

        });
        dialog.setPositiveButton("Ok", (dialog12, which) -> {
            DBHelper db = DBHelper.getINSTANCE();
            int numberOfFilesRemoved = 0;
            for (int i = 0; i < selectedFilesList.length; i++) {
                if (selectedFilesList[i]) {
                    // Getting desired file
                    SavedFile savedFile = savedFileList.get(i - numberOfFilesRemoved);
                    File file = new File(savedFile.getPath());
                    //Deleting file from Database Record
                    db.deleteFileFromDB(SavedFilesActivity.FILES_TABLE_NAME, savedFile.getId());
                    // Deleting original file
                    file.delete();
                    notifyItemRemoved(i - numberOfFilesRemoved);

                    // Removing file from recyclerView list
                    savedFileList.remove(i - numberOfFilesRemoved);
                    numberOfFilesRemoved += 1;
                }
            }

            Toast.makeText(context, "Delete Successful", Toast.LENGTH_SHORT).show();

            selectedFilesList = new boolean[savedFileList.size()];

            notifyItemRangeChanged(0, savedFileList.size());
            mode.finish();

        });

        dialog.create().show();
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
}
