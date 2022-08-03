package com.hz_apps.timebasedlocker.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hz_apps.timebasedlocker.R;
import com.hz_apps.timebasedlocker.ui.LockFiles.LockFilesActivity;

import java.io.File;
import java.util.ArrayList;

public class FilesListAdapter extends RecyclerView.Adapter<FilesListAdapter.myViewHolder>{

    private final Context context;
    private final ArrayList<File> imagesFilesList;
    private boolean isAllItemsSelected = false;
    private boolean isAllItemsUnSelected = false;
    private ArrayList<File> selectedFiles;
    private final ImageButton nextBtn;

    public FilesListAdapter(Context context, ArrayList<File> imagesFilesList, ImageButton nextBtn) {
        this.context = context;
        this.imagesFilesList = imagesFilesList;
        this.selectedFiles = new ArrayList<>();
        this.nextBtn = nextBtn;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_list_view, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        File file = imagesFilesList.get(position);

        Glide.with(context).load(file)
                .into(holder.imageView);

        holder.imageView.setOnClickListener(view -> holder.checkBox.setChecked(!holder.checkBox.isChecked()));

        // handling click listener on checkbox
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // add file into list if checkbox is checked
            if (isChecked){
                selectedFiles.add(file);
            }else{
                selectedFiles.remove(file);
            }

            // if more than one file is selected then make nextBtn available
            if (selectedFiles.size() == 0){
                nextBtn.setBackgroundResource(R.drawable.round_button_disabled);
            }
            else{
                nextBtn.setBackgroundResource(R.drawable.round_button_enabled);
            }
        });

        // setting click listener on next button
        nextBtn.setOnClickListener(v -> {
            if (selectedFiles.size() == 0 ){

            }else{
                Intent intent = new Intent(context, LockFilesActivity.class);
                intent.putExtra("selected_files", selectedFiles);
                context.startActivity(intent);
            }
        });

        setAllItemsSelected(holder.checkBox);


    }

    @Override
    public int getItemCount() {
        return imagesFilesList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final CheckBox checkBox;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView_images_list);
            checkBox = itemView.findViewById(R.id.checkbox_images_list);
        }
    }

    private void setAllItemsSelected(CheckBox checkBox){
        if (isAllItemsSelected){
            checkBox.setChecked(true);
            isAllItemsUnSelected = false;
        }
    }


    public void setAllItemsSelected(boolean b){
        isAllItemsSelected  = b;
    }

    public void setAllItemsUnSelected(boolean allItemsUnSelected) {
        isAllItemsUnSelected = allItemsUnSelected;
    }
}