package com.hz_apps.timebasedlocker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hz_apps.timebasedlocker.R;

import java.io.File;
import java.util.ArrayList;

public class FilesListAdapter extends RecyclerView.Adapter<FilesListAdapter.myViewHolder>{

    private final Context context;
    private final ArrayList<File> imagesFilesList;
    private boolean isAllItemsSelected = false;
    private boolean isAllItemsUnSelected = false;

    public FilesListAdapter(Context context, ArrayList<File> imagesFilesList) {
        this.context = context;
        this.imagesFilesList = imagesFilesList;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_list_view, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Glide.with(context).load(imagesFilesList.get(position)).into(holder.imageView);

        holder.imageView.setOnClickListener(view -> {
            if (holder.checkBox.isChecked()) holder.checkBox.setChecked(false);
            else holder.checkBox.setChecked(true);
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